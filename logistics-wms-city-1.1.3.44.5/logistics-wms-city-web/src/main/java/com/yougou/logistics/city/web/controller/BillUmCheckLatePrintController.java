package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmCheckLatePrint;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillUmCheckLatePrintManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-8-14 上午10:52:49
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/billumchecklateprint")
@ModuleVerify("25130180")
public class BillUmCheckLatePrintController extends BaseCrudController<BillUmCheckLatePrint> {
	
	@Log
	private Logger log;
	
	@Resource
	private BillUmCheckLatePrintManager billUmCheckLatePrintManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("billumchecklateprint/",billUmCheckLatePrintManager);
    }

	/**
	 * 查询明细
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billUmCheckLatePrintManager.findCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheckLatePrint> list = billUmCheckLatePrintManager.findByPage(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			for (BillUmCheckLatePrint dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("untreadNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("checkQty", totalCheckQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billUmCheckLatePrintManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String itemQty = "0";
					if (sumFoot1.get("itemQty") != null) {
						itemQty = sumFoot1.get("itemQty").toString();
					}
					String checkQty = "0";
					if (sumFoot1.get("checkQty") != null) {
						checkQty = sumFoot1.get("checkQty").toString();
					}
					sumFoot.put("itemQty", itemQty);
					sumFoot.put("checkQty", checkQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("untreadNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 查询明细
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getBarcodeList.json")
	@ResponseBody
	public Map<String, Object> getBarcodeList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			String barcodes = StringUtils.isEmpty(req.getParameter("barcodes")) ? "" : String.valueOf(req.getParameter("barcodes"));
			Map<String, Object> params = builderParams(req, model);
			String[] barcodeList = barcodes.split("@");
			List<BillUmCheckLatePrint> returnList = new ArrayList<BillUmCheckLatePrint>();
			if(barcodeList.length > 0){
				List<String> list = new ArrayList<String>();
				for (String barcode : barcodeList) {
					if(StringUtils.isNotBlank(barcode)){
						if(!list.contains(barcode)){
							list.add(barcode);
						}
					}
				}
				if(CommonUtil.hasValue(list)){
					returnList = billUmCheckLatePrintManager.findItemInfoByBarcode(params, list);
				}
			}
			obj.put("list", returnList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
}
