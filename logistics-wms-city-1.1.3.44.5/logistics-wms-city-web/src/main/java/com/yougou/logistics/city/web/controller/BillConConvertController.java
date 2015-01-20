package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.BillConvertStatusEnums;
import com.yougou.logistics.city.common.model.BillConConvert;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtil;
import com.yougou.logistics.city.manager.BillConConvertManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
@RequestMapping("/bill_con_convert")
@ModuleVerify("25110050")
public class BillConConvertController extends BaseCrudController<BillConConvert> {
    @Resource
    private BillConConvertManager billConConvertManager;

    @Resource
    private ProcCommonManager procCommonManager;
    
    @Log
	private Logger log;
    
    @Override
    public CrudInfo init() {
        return new CrudInfo("billconconvert/",billConConvertManager);
    }
    
    @RequestMapping(value = "/add")
	@ResponseBody    
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object saveMainInfo(HttpServletRequest req,BillConConvert conConvert){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	String result = "";
		String convertNo = "";
		try {
			Date date = new Date();
			CurrentUser currentUser=new CurrentUser(req);
			convertNo = procCommonManager.procGetSheetNo(conConvert.getLocno(), CNumPre.CON_CONVERT_PRE);
			conConvert.setConvertNo(convertNo);
			conConvert.setStatus(BillConvertStatusEnums.STATUS_10.getValue());
			conConvert.setCreatorName(currentUser.getUsername());
			conConvert.setEditor(currentUser.getLoginName());
			conConvert.setEditorName(currentUser.getUsername());
			conConvert.setCreatetm(date);
			conConvert.setEdittm(date);
			billConConvertManager.add(conConvert);
			result = "success";
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result = CommonUtil.getExceptionMessage(e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = CommonUtil.getExceptionMessage(e);
		}
		obj.put("result", result);
		obj.put("convertNo", convertNo);
		return obj;
	}
    
    @RequestMapping(value = "/modify")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.MODIFY)
   	public Object modify(HttpServletRequest req,BillConConvert conConvert){
       	Map<String, Object> obj = new HashMap<String, Object>();
       	String result = "";
   		try {
   			conConvert.setEdittm(new Date());
   			conConvert.setUpdStatus("10");
   			int count = billConConvertManager.modifyById(conConvert);
   			if(count > 0){
   				result = "success";
   			}else{
   				result = "单据"+conConvert.getConvertNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作";
   			}
   		} catch (ManagerException e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		}
   		obj.put("result", result);
   		return obj;
   	}
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
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
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String brandNo = "";
			String sysNo = "";
			if(paramsAll.get("brandNo") != null) {
				brandNo = paramsAll.get("brandNo").toString();
			}
			if(paramsAll.get("sysNo") != null) {
				sysNo = paramsAll.get("sysNo").toString();
			}
			if(!brandNo.equals("") || !sysNo.equals("")) {
				params.put("joinIn", "true");
			}
			int total = this.billConConvertManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConConvert> list = this.billConConvertManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("convertNo", "小计");
			footerList.add(footerMap);
			for (BillConConvert c : list) {
				SumUtil.setFooterMap("itemQty", c.getItemQty(), footerMap);
			}
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				if(pageSize >= total){
					sumFoot.putAll(footerMap);
				}else{
					Map<String, Object> map = billConConvertManager.findSumQty(params, authorityParams);
					SumUtil.setSumMap(map, sumFoot);
				}
				sumFoot.put("convertNo", "总计");
				footerList.add(sumFoot);
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
    @RequestMapping(value = "/removeMainAndDtl")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.REMOVE)
   	public Object removeMainAndDtl(HttpServletRequest req){
       	Map<String, Object> obj = new HashMap<String, Object>();
       	String result = "";
   		try {
   			String keyStr = StringUtils.isEmpty(req.getParameter("keyStr")) ? "" : String.valueOf(req.getParameter("keyStr"));
   			billConConvertManager.removeMainAndDtl(keyStr);
   			result = "success";
   		} catch (ManagerException e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		}
   		obj.put("result", result);
   		return obj;
   	}
    @RequestMapping(value = "/check")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.VERIFY)
   	public Object check(HttpServletRequest req){
       	Map<String, Object> obj = new HashMap<String, Object>();
       	String result = "";
   		try {
   			String keyStr = StringUtils.isEmpty(req.getParameter("keyStr")) ? "" : String.valueOf(req.getParameter("keyStr"));
   			String operator = StringUtils.isEmpty(req.getParameter("operator")) ? "" : String.valueOf(req.getParameter("operator"));
   			String auditorName = StringUtils.isEmpty(req.getParameter("auditorName")) ? "" : String.valueOf(req.getParameter("auditorName"));
   			billConConvertManager.check(keyStr, operator,0 , null,auditorName);
   			result = "success";
   		} catch (ManagerException e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			result = CommonUtil.getExceptionMessage(e);
   		}
   		obj.put("result", result);
   		return obj;
   	}
}