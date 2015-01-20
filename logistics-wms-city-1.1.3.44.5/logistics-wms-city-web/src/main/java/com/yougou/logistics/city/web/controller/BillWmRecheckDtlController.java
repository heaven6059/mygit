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
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillWmRecheckDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 反配复核单明细
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@RequestMapping("/wmrecheckdtl")
public class BillWmRecheckDtlController extends BaseCrudController<BillWmRecheckDtl> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillWmRecheckDtlManager billWmRecheckDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("wmrecheckdtl/",billWmRecheckDtlManager);
    }
    
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmRecheckDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRecheck> list = this.billWmRecheckDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
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
			int total = billWmRecheckDtlManager.findWmRecheckDtlGroupByCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRecheckDtl> list = billWmRecheckDtlManager.findWmRecheckDtlGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
    		
    		//小计
    		List<Object> footerList = new ArrayList<Object>();
    		Map<String, Object> footer = new HashMap<String, Object>();
    		BigDecimal totalItemQty = new BigDecimal(0);
    		for(BillWmRecheckDtl dtl:list){
    			totalItemQty = totalItemQty.add(dtl.getItemQty());
    		}
    		footer.put("scanLabelNo", "小计");
    		footer.put("itemQty", totalItemQty);
    		footerList.add(footer);
    		obj.put("footer", footerList);
    		
    		// 合计
    		Map<String, Object> sumFoot1 = new HashMap<String, Object>();
    		if (pageNo == 1) {
    			sumFoot1 = billWmRecheckDtlManager.selectWmRecheckDtlSumQty(params, authorityParams);
    			if (sumFoot1 != null) {
    				Map<String, Object> sumFoot2 = new HashMap<String, Object>();
    				sumFoot2.put("itemQty", sumFoot1.get("itemQty"));
    				sumFoot2.put("isselectsum", true);
    				sumFoot1 = sumFoot2;
    			}
    		}
    		sumFoot1.put("scanLabelNo", "合计");
    		footerList.add(sumFoot1);
    		obj.put("footer", footerList);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
		return obj;
	}
    
    
    @RequestMapping(value = "/findWmRecheckDtlByPage")
   	@ResponseBody    
   	public  Map<String, Object> findWmRecheckDtlByPage(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
       		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
       		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
       		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
       		Map<String, Object> params = builderParams(req, model);
       		int total = this.billWmRecheckDtlManager.findWmRecheckDtlCount(params, authorityParams);
       		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
       		List<BillWmRecheckDtl> list = this.billWmRecheckDtlManager.findWmRecheckDtlByPage(page, sortColumn, sortOrder, params, authorityParams);
       		obj.put("total", total);
       		obj.put("rows", list);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
   		return obj; 
   	}  
    
    @RequestMapping(value = "/printByBox")
	@ResponseBody
	public  Map<String, Object> printByBox(HttpServletRequest req,BillWmRecheck billWmRecheck) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		String result = "";
		String locno = req.getParameter("locno");
		String keys = req.getParameter("keys");
		boolean noneDtl = "true".equals(req.getParameter("noneDtl"));
		try{
			if(StringUtils.isBlank(locno)||
					StringUtils.isBlank(keys)){
				result = "缺少参数";
				obj.put("result", result);
			}else{
				obj = billWmRecheckDtlManager.printByBox(locno, billWmRecheck, keys,noneDtl);
			}
		}catch(ManagerException e){
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		}
		return obj;
	}
}