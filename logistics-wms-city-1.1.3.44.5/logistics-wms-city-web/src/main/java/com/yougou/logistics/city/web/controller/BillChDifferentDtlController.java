package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.model.BillChDifferentDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillChDifferentDtlMapper;
import com.yougou.logistics.city.manager.BillChDifferentDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/bill_ch_different_dtl")
public class BillChDifferentDtlController extends BaseCrudController<BillChDifferentDtl> {
	@Resource
	private BillChDifferentDtlManager billChDifferentDtlManager;
	@Resource
	private BillChDifferentDtlMapper billChDifferentDtlMapper;
	@Log
	private Logger log;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billchdifferentdtl/", billChDifferentDtlManager);
	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			String planType = params.get("planType")==null?BillChPlanTypeEnums.ITEM.getValue():params.get("planType").toString();
			if(StringUtils.isBlank(planType)){
				planType = BillChPlanTypeEnums.ITEM.getValue();
			}
			int total = 0;
			List<BillChDifferentDtl> list = null;
			SimplePage page = null;
			if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
				total = this.billChDifferentDtlManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
				page = new SimplePage(pageNo, pageSize, (int) total);
				list = this.billChDifferentDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
			}else{
				total = this.billChDifferentDtlManager.findCount(params);
				page = new SimplePage(pageNo, pageSize, (int) total);
				list = this.billChDifferentDtlManager.findByPage(page, sortColumn, sortOrder, params);
			}

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalDiffQty = new BigDecimal(0);
			for (BillChDifferentDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty());
				totalDiffQty = totalDiffQty.add(dtl.getDiffQty());
			}
			footer.put("cellNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("diffQty", totalDiffQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
					sumFoot = billChDifferentDtlMapper.selectSumQty(params, authorityParams);
				}else{
					sumFoot = billChDifferentDtlMapper.selectSumQty(params, null);
				}
				if (null == sumFoot) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_Qty", 0);
					sumFoot.put("real_Qty", 0);
					sumFoot.put("diff_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("cell_no", "合计");
			} else {
				sumFoot.put("cellNo", "合计");
			}

			footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}