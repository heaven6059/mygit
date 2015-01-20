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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.BillChCheckDirectStatusEnums;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.dal.mapper.BillChCheckDirectMapper;
import com.yougou.logistics.city.manager.BillChCheckDirectManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 14:54:50 CST 2013
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
@RequestMapping("/bill_ch_check_direct")
@ModuleVerify("25120020")
public class BillChCheckDirectController extends BaseCrudController<BillChCheckDirect> {
	@Log
	private Logger log;
	@Resource
	private BillChCheckDirectManager billChCheckDirectManager;
	@Resource
    private BillChCheckDirectMapper billChCheckDirectMapper;
	@Override
	public CrudInfo init() {
		return new CrudInfo("billchrequest/", billChCheckDirectManager);
	}

	@RequestMapping(value = "/direct")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object direct(BillChPlan billChPlan, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object result = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billChCheckDirectManager.createDirect(billChPlan,user, authorityParams);
			result = "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/cancelDirect")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Object cancelDirect(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object result = null;
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String locno = req.getParameter("locno");
			String planNo = req.getParameter("planNo");
			String paramStr = req.getParameter("paramStr");
			if(StringUtils.isBlank(locno)){
				result = "缺少【仓库编码】";
			}else if(StringUtils.isBlank(planNo)){
				result = "缺少【盘点计划单号】";
			}else if(StringUtils.isBlank(paramStr)){
				result = "缺少【定位单】信息";
			}else {
				billChCheckDirectManager.changeDirect(
									paramStr, 
									locno, 
									planNo, 
									BillChCheckDirectStatusEnums.CREATE.getStatus(), 
									BillChCheckDirectStatusEnums.CANCEL.getStatus(),
									user
								);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/regainDirect")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Object regainDirect(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object result = null;
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String locno = req.getParameter("locno");
			String planNo = req.getParameter("planNo");
			String paramStr = req.getParameter("paramStr");
			if(StringUtils.isBlank(locno)){
				result = "缺少【仓库编码】";
			}else if(StringUtils.isBlank(planNo)){
				result = "缺少【盘点计划单号】";
			}else if(StringUtils.isBlank(paramStr)){
				result = "缺少【定位单】信息";
			}else {
				billChCheckDirectManager.changeDirect(
									paramStr, 
									locno, 
									planNo, 
									BillChCheckDirectStatusEnums.CANCEL.getStatus(), 
									BillChCheckDirectStatusEnums.CREATE.getStatus(),
									user
								);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	/**
	 * 查询定位中的所有计划单
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectPlanNo")
	@ResponseBody
	public Map<String, Object> selectPlanNo(HttpServletRequest req, Model model){
		Map<String, Object> obj=null;
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map params = builderParams(req, model);
			int total = billChCheckDirectManager.selectPlanNoCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDirect> list = billChCheckDirectManager.selectPlanNo(params, page);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
    @RequestMapping(value = "/dtl_list.json")
    @ResponseBody
    public Map<String, Object> queryDtlList(HttpServletRequest req, Model model){
    Map<String, Object> obj=null;
    try {
    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
			: Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
			: Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
			: String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
			: String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		String planType = params.get("planType")==null?BillChPlanTypeEnums.ITEM.getValue():params.get("planType").toString();
		if(StringUtils.isBlank(planType)){
			planType = BillChPlanTypeEnums.ITEM.getValue();
		}
		int total = 0;
		List<BillChCheckDirect> list = null;
		SimplePage page = null;
		if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
			total = this.billChCheckDirectManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
			page = new SimplePage(pageNo, pageSize, (int) total);
			list = this.billChCheckDirectManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
		}else{
			total = this.billChCheckDirectManager.findCount(params);
			page = new SimplePage(pageNo, pageSize, (int) total);
			list = this.billChCheckDirectManager.findByPage(page, sortColumn, sortOrder, params);
		}
		List<Object> footerList = new ArrayList<Object>();
		Map<String, Object> footer = new HashMap<String, Object>();
		BigDecimal totalItemQty = new BigDecimal(0);
		for (BillChCheckDirect dtl : list) {
		    totalItemQty = totalItemQty.add(dtl.getItemQty());
		}
		footer.put("planNo", "小计");
		footer.put("itemQty", totalItemQty);
		footerList.add(footer);
	
		// 合计
		Map<String, Object> sumFoot = new HashMap<String, Object>();
		if (pageNo == 1) {
			if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
				sumFoot = billChCheckDirectMapper.selectSumQty(params, authorityParams);
			}else{
				sumFoot = billChCheckDirectMapper.selectSumQty(params, null);
			}
		    sumFoot.put("isselectsum", true);
		    sumFoot.put("plan_no", "合计");
		} else {
		    sumFoot.put("planNo", "合计");
		}
		footerList.add(sumFoot);
		obj = new HashMap<String, Object>();
		obj.put("footer", footerList);
		obj.put("total", total);
		obj.put("rows", list);
    } catch (Exception e) {
		log.error(e.getMessage(), e);
	}
	return obj;
    }
}