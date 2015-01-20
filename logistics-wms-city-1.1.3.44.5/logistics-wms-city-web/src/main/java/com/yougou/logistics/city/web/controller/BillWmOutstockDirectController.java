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
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillWmOutstockDirectManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 退厂拣货下架指示类 
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
@RequestMapping("/bill_wm_outstock_direct")
public class BillWmOutstockDirectController extends BaseCrudController<BillWmOutstockDirect> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillWmOutstockDirectManager billWmOutstockDirectManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billWmOutstockDirect/",billWmOutstockDirectManager);
    }
    
    @RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String,Object>(0);
    	try{
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmOutstockDirectManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> otherinList = this.billWmOutstockDirectManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);			
			obj.put("total", total);
			obj.put("rows", otherinList);    		
    		
    		List<Object> footerList = new ArrayList<Object>();
    		Map<String, Object> footer = new HashMap<String, Object>();
    		BigDecimal totalOutstockItemQty = new BigDecimal(0);
    		List<BillWmOutstockDirect> list = CommonUtil.getRowsByListJson(obj, BillWmOutstockDirect.class);
    		for(BillWmOutstockDirect dtl:list){
    			totalOutstockItemQty = totalOutstockItemQty.add(dtl.getOutstockItemQty());
    		}
    		footer.put("sCellNo", "汇总");
    		footer.put("outstockItemQty", totalOutstockItemQty);
    		footerList.add(footer);
    		obj.put("footer", footerList);
    	}catch(Exception e){
    		log.error(e.getMessage(),e);
    	}
		return obj;
	}
}