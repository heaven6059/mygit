package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillChPlanManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
@RequestMapping("/bill_ch_plan")
@ModuleVerify("25120010")
public class BillChPlanController extends BaseCrudController<BillChPlan> {
	@Log
	private Logger log;
	@Resource
    private BillChPlanManager billChPlanManager;
    
    @Resource
    private ProcCommonManager procCommonManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchplan/",billChPlanManager);
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
			Map<String, Object> params = builderParams(req, model);
			int total = this.billChPlanManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChPlan> list = this.billChPlanManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
    @RequestMapping(value = "/saveMainInfo")
	@ResponseBody    
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object saveMainInfo(HttpServletRequest req,BillChPlan billChPlan) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
		String planNo = "";
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			planNo = procCommonManager.procGetSheetNo(billChPlan.getLocno(), CNumPre.CH_PLAN_PRE);
			billChPlan.setPlanNo(planNo);
			billChPlan.setStatus("10");//新建状态
			billChPlan.setOwnerNo("BL");
			billChPlan.setCreatetm(new Date());
			billChPlan.setEdittm(new Date());
			billChPlanManager.saveMain(billChPlan, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			return obj;
		}
		obj.put("success", true);
		obj.put("planNo", planNo);
		return obj;
	}
    
    /**
     * 设置更新人信息及更新时间
     * @param req
     * @param billChPlan
     * @return
     */
    private BillChPlan setModiflyInfo(HttpServletRequest req,BillChPlan billChPlan){
    	SystemUser loginUser= (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
		if(loginUser!=null){
			billChPlan.setEditor(loginUser.getLoginName());
			billChPlan.setEditorName(loginUser.getUsername());
			billChPlan.setEdittm(new Date());
		}
		return billChPlan;
    }
    
    @RequestMapping(value = "/editMainInfo")
	@ResponseBody    
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Object editMainInfo(HttpServletRequest req,BillChPlan billChPlan) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			//billChPlan.setEdittm(new Date());
			billChPlan.setSourceStatus(BillChPlanStatusEnums.CREATE.getValue());
			//设置更新人信息及更新时间
			billChPlan=setModiflyInfo(req, billChPlan);
			
			billChPlanManager.editMainInfo(billChPlan, authorityParams);
//			int i = billChPlanManager.modifyById(billChPlan);
//			if(i <= 0){
//				obj.put("success", "false");
//				obj.put("errorMsg", "盘点计划单【"+billChPlan.getPlanNo()+"】已不存在或状态不为【"+BillChPlanStatusEnums.CREATE.getText()+"】");
//				return obj;
//			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("success", "false");
			obj.put("errorMsg", e.getMessage());
			return obj;
		}
		obj.put("success", "true");
		return obj;
	}
    
    @RequestMapping(value = "/check")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.VERIFY)
    public Object check(HttpServletRequest req,BillChPlan billChPlan) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	String planNos = StringUtils.isEmpty(req.getParameter("planNos")) ? "" : req.getParameter("planNos");
    	if(StringUtils.isBlank(planNos)){
    		result = "请选择计划单号";
    	}else{
    		try {
    			//billChPlan.setEdittm(new Date());
    			//设置更新人信息及更新时间
    			billChPlan=setModiflyInfo(req, billChPlan);
    			billChPlan.setAudittm(new Date());
				billChPlanManager.check(billChPlan, planNos);
				result = "success";
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				result = e.getCause().getMessage();
			}
    	}
    	map.put("result", result);
    	return map;
    }
    
    @RequestMapping(value = "/invalid")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.VERIFY)
    public Object invalid(HttpServletRequest req,BillChPlan billChPlan) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	String planNosStr = StringUtils.isEmpty(req.getParameter("planNos")) ? "" : req.getParameter("planNos");
    	if(StringUtils.isBlank(planNosStr)){
    		result = "请选择计划单号";
    	}else{
    		try {
    			HttpSession session = req.getSession();
    			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    			String [] planNos = planNosStr.split(",");
    			//billChPlan.setEdittm(new Date());
    			//设置更新人信息及更新时间
    			billChPlan=setModiflyInfo(req, billChPlan);
    			billChPlan.setAudittm(new Date());
				billChPlanManager.invalid(billChPlan, planNos, user);
				result = "success";
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				result = e.getCause().getMessage();
			}
    	}
    	map.put("result", result);
    	return map;
    }
    @RequestMapping(value = "/deleteMain")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.REMOVE)
    public Object deleteMain(HttpServletRequest req,BillChPlan billChPlan) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	String planNos = StringUtils.isEmpty(req.getParameter("planNos")) ? "" : req.getParameter("planNos");
    	if(StringUtils.isBlank(planNos)){
    		result = "请选择计划单号";
    	}else{
    		try {
    			billChPlanManager.deleteMain(billChPlan, planNos);
    			result = "success";
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			result = e.getCause().getMessage();
    		}
    	}
    	map.put("result", result);
    	return map;
    }
}