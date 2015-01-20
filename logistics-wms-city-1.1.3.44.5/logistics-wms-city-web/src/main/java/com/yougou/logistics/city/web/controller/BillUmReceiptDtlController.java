package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.yougou.logistics.city.common.model.BillUmReceipt;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.model.BillUmReceiptKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmReceiptDtlManager;
import com.yougou.logistics.city.manager.BillUmReceiptManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
 * @version 1.0.6
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
@RequestMapping("/bill_um_receipt_dtl")
public class BillUmReceiptDtlController extends BaseCrudController<BillUmReceiptDtl> {
	@Log
	private Logger log;
    @Resource
    private BillUmReceiptDtlManager billUmReceiptDtlManager;
    @Resource
    private BillUmReceiptManager billUmReceiptManager;

    private static final String STATUS10 = "10";
    @Override
    public CrudInfo init() {
        return new CrudInfo("billUmReceiptDtl/",billUmReceiptDtlManager);
    }
    @RequestMapping(value = "/saveDtl")
  	@ResponseBody
  	public Map<String, Object> save(BillUmReceipt billUmReceipt, HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			Date d = new Date();
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
  			
  			String boxStr = req.getParameter("boxStr");
  			String locno = billUmReceipt.getLocno();
  			String receiptNo = billUmReceipt.getReceiptNo();
  			String untreadNo = billUmReceipt.getUntreadNo();
  			String ownerNo = billUmReceipt.getOwnerNo();
  			if(StringUtils.isBlank(locno)){
  				result = "缺少仓库信息";
  			}else if(StringUtils.isBlank(receiptNo)){
  				result = "缺少收货单信息";
  			}else if(StringUtils.isBlank(untreadNo)){
  				result = "缺少店退仓单信息";
  			}else if(StringUtils.isBlank(ownerNo)){
  				result = "缺少货主信息";
  			}else{
  				billUmReceipt.setCreatetm(d);
  				BillUmReceiptKey billUmReceiptKey = new BillUmReceiptKey();
  				billUmReceiptKey.setLocno(billUmReceipt.getLocno());
  				billUmReceiptKey.setOwnerNo(billUmReceipt.getOwnerNo());
  				billUmReceiptKey.setReceiptNo(billUmReceipt.getReceiptNo());
  				BillUmReceipt bReceipt =(BillUmReceipt) billUmReceiptManager.findById(billUmReceiptKey);
  				if (null == bReceipt ||!STATUS10.equals(bReceipt.getStatus())){
  					result = "notExist";
  					map.put("result", "单据:"+billUmReceipt.getReceiptNo() +"已删除或者状态发生改变！");
  					return map;
  				}
  				billUmReceiptDtlManager.save(billUmReceipt, boxStr, user);
  				result = "success";
  			}
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = e.getCause().getMessage();
  		}
  		map.put("result", result);
  		return map;
  	}
    @RequestMapping(value = "/listByBox.json")
	@ResponseBody
	public  Map<String, Object> queryListByBox(HttpServletRequest req, Model model) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billUmReceiptDtlManager.findCountByBox(params);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillUmReceiptDtl> list = this.billUmReceiptDtlManager.findByPageByBox(page, params);
    		obj.put("total", total);
    		obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    @RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = billUmReceiptDtlManager.findCount(params, authorityParams,DataAccessRuleEnum.BRAND);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillUmReceiptDtl> list = billUmReceiptDtlManager.findByPage(page, sortColumn, sortOrder, params, authorityParams,DataAccessRuleEnum.BRAND);
    		obj.put("total", total);
    		obj.put("rows", list);
//    		Map<String, Object> obj = super.queryList(req, model);
//    		List<BillUmReceiptDtl> list = CommonUtil.getRowsByListJson(obj, BillUmReceiptDtl.class);
    		
    		List<Object> footerList = new ArrayList<Object>();
    		//当页小计
    		BigDecimal totalItemQty = new BigDecimal(0);
    		for(BillUmReceiptDtl dtl:list){
    			totalItemQty = totalItemQty.add(dtl.getItemQty());
    		}
    		Map<String, Object> footer = new HashMap<String, Object>();
    		footer.put("boxNo", "小计");
    		footer.put("itemQty", totalItemQty);
    		footerList.add(footer);
    		// 合计
    		Map<String, Object> sumFoot = new HashMap<String, Object>();
    		if (pageNo == 1) {
    			sumFoot = billUmReceiptDtlManager.selectSumQty(params, authorityParams);
    			if (sumFoot != null) {
    				sumFoot.put("isselectsum", true);
    			}
    		}
    		
    		sumFoot.put("boxNo", "合计");
    		footerList.add(sumFoot);
    		obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}