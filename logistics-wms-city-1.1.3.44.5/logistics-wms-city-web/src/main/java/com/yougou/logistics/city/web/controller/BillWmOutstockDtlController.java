package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmOutstockDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Controller
@RequestMapping("/bill_wm_outstock_dtl")
public class BillWmOutstockDtlController extends BaseCrudController<BillWmOutstockDtl> {
	@Log
	private Logger log;
	@Resource
	private BillWmOutstockDtlManager billWmOutstockDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billwmoutstockdtl/", billWmOutstockDtlManager);
	}
	
	@RequestMapping(value = "/findOutstockDtlItem")
   	@ResponseBody    
   	public  List<BillWmOutstockDtlDto> findOutstockDtlItem(HttpServletRequest req, BillWmOutstockDtl billWmOutstockDtl) throws ManagerException {
		List<BillWmOutstockDtlDto> result = new ArrayList<BillWmOutstockDtlDto>(0);
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			result = billWmOutstockDtlManager.findOutstockDtlItem(billWmOutstockDtl, authorityParams);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
   		return result;
   	}
	@RequestMapping(value = "/dtl_findOutstockDtlItem.json")
   	@ResponseBody    
   	public  Map<String, Object> findOutstockDtlItemDtl(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		try{
   			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billWmOutstockDtlManager.findOutstockDtlItemCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmOutstockDtlDto> list = billWmOutstockDtlManager.findOutstockDtlItemByPage(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			
   			//小计
   			List<Object> footerList = new ArrayList<Object>();
   			Map<String, Object> footer = new HashMap<String, Object>();
   			BigDecimal totalRealQty = new BigDecimal(0);
   			BigDecimal totalRecheckQty = new BigDecimal(0);
   			BigDecimal totalDiffQty = new BigDecimal(0);
   			for(BillWmOutstockDtlDto dtl:list){
   				totalRealQty = totalRealQty.add(dtl.getRealQty()==null?new BigDecimal(0):dtl.getRealQty());
   				totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
   				totalDiffQty = totalDiffQty.add(new BigDecimal(dtl.getDiffQty()));
   			}
   			footer.put("itemNo", "小计");
   			footer.put("realQty", totalRealQty);
   			footer.put("recheckQty", totalRecheckQty);
   			footer.put("diffQty", totalDiffQty);
   			footerList.add(footer);
   			
   			// 合计
   			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
   			if (pageNo == 1) {
   				sumFoot1 = billWmOutstockDtlManager.selectOutstockDtlItemSumQty(params,authorityParams);
   				if (sumFoot1 != null) {
   					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
   					sumFoot2.put("realQty", sumFoot1.get("realQty"));
   					sumFoot2.put("recheckQty", sumFoot1.get("recheckQty"));
   					sumFoot2.put("diffQty", sumFoot1.get("diffQty"));
   					sumFoot2.put("isselectsum", true);
   					sumFoot1 = sumFoot2;
   				}
   			}
   			sumFoot1.put("itemNo", "合计");
   			footerList.add(sumFoot1);
   			obj.put("footer", footerList);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
   		return obj;
   	}
	
	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj =  new HashMap<String,Object>(0);
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billWmOutstockDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmOutstockDtlDto> list = billWmOutstockDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for(BillWmOutstockDtl dtl:list){
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty()==null?new BigDecimal(0):dtl.getRealQty());
			}
			footer.put("sourceNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billWmOutstockDtlManager.selectSumQty(params,authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("itemQty", sumFoot1.get("itemQty"));
					sumFoot2.put("realQty", sumFoot1.get("realQty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("sourceNo", "合计");
			footerList.add(sumFoot1);
			obj.put("footer", footerList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Object printDetail(HttpServletRequest req, HttpSession session,String keys,String locno) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	String result = "";
    	try {
    		Object u = session.getAttribute(PublicContains.SESSION_USER);
    		SystemUser user = null;
    		if(u != null){
    			user = (SystemUser)u;
    		}
			if(StringUtils.isBlank(keys)){
				result = "请传入正确的退仓拣货单号!";
			}else if(StringUtils.isBlank(locno)){
				result = "请传入正确的仓库编号!";
			}else{
				List<String> htmlList = billWmOutstockDtlManager.printDetail(locno, keys,user);
				if(htmlList.size() > 0){
					obj.put("data", htmlList);
					result = "success";
				}else{
					result = "没有任何明细!";
				}
				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	obj.put("result", result);
    	return obj;
    }
}