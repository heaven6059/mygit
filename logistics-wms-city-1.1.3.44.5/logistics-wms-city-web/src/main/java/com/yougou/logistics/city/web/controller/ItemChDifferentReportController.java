package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ItemChDifferentReport;
import com.yougou.logistics.city.manager.ItemChDifferentReportManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/item_ch_different_report")
@ModuleVerify("25130070")
public class ItemChDifferentReportController extends
		BaseCrudController<ItemChDifferentReport> {
	@Log
	private Logger log;
	@Resource
	private ItemChDifferentReportManager itemChDifferentReportManager;

	@Override
	protected CrudInfo init() {
		return new CrudInfo("itemchdifferentreport/",
				itemChDifferentReportManager);
	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();

		try {
			AuthorityParams authorityParams = UserLoginUtil
					.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
					: Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
					: Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
					: String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
					: String.valueOf(req.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String itemNo = "";
			if(paramsAll.get("itemNo") != null) {
				itemNo = paramsAll.get("itemNo").toString().toUpperCase();
				params.put("itemNo", itemNo);
			}
			String barcode = "";
			if(paramsAll.get("barcode") != null) {
				barcode = paramsAll.get("barcode").toString().toUpperCase();
				params.put("barcode", barcode);
			}
			int total = itemChDifferentReportManager.findCount(params,
					authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<ItemChDifferentReport> list = itemChDifferentReportManager
					.findByPage(page, sortColumn, sortOrder, params,
							authorityParams, DataAccessRuleEnum.BRAND);

			obj.put("total", total);
			obj.put("rows", list);

			List<Object> footerList = new ArrayList<Object>();

			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalDiffQty = new BigDecimal(0);
			for (ItemChDifferentReport dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty());
				totalDiffQty = totalDiffQty.add(dtl.getDiffQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("planNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("diffQty", totalDiffQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = itemChDifferentReportManager.selectSumQty(params,
						authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("itemQty", sumFoot1.get("itemQty"));
					sumFoot2.put("realQty", sumFoot1.get("realQty"));
					sumFoot2.put("diffQty", sumFoot1.get("diffQty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("planNo", "合计");
			footerList.add(sumFoot1);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/do_export_dtl")
	public void doExportDtl(HttpServletRequest req, Model model,
			HttpServletResponse response) throws ManagerException {
		Map<String, Object> paramsAll = builderParams(req, model);
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(paramsAll);
		String itemNo = "";
		if(paramsAll.get("itemNo") != null) {
			itemNo = paramsAll.get("itemNo").toString().toUpperCase();
			params.put("itemNo", itemNo);
		}
		String barcode = "";
		if(paramsAll.get("barcode") != null) {
			barcode = paramsAll.get("barcode").toString().toUpperCase();
			params.put("barcode", barcode);
		}
		String exportColumns = (String) params.get("exportColumns");
		String fileName = (String) params.get("fileName");
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				AuthorityParams authorityParams = UserLoginUtil
						.getAuthorityParams(req);
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";

				// 字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns,
						new TypeReference<List<Map>>() {
						});

				// List<ModelType> list= this .manager .findByBiz(modelType,
				// params);
				int total = this.itemChDifferentReportManager.findCount(params,
						authorityParams, DataAccessRuleEnum.BRAND);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<ItemChDifferentReport> list = this.itemChDifferentReportManager
						.findByPage(page, "", "", params, authorityParams,
								DataAccessRuleEnum.BRAND);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (ItemChDifferentReport vo : list) {
						Map map = new HashMap();
						BeanUtilsCommon.object2MapWithoutNull(vo, map);
						listArrayList.add(map);

					}
					HSSFExport.commonExportData(StringUtils
							.isNotEmpty(fileName) ? fileName : "导出信息",
							ColumnsList, listArrayList, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

	}
}
