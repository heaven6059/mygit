package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmPlanManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
@RequestMapping("/bill_wm_plan")
@ModuleVerify("25090060")
public class BillWmPlanController extends BaseCrudController<BillWmPlan> {
    @Resource
    private BillWmPlanManager billWmPlanManager;

    @Log
    private Logger log;

    @Override
    public CrudInfo init() {
	return new CrudInfo("billwmplan/", billWmPlanManager);
    }
    
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmPlanManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> list = this.billWmPlanManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);

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
	
    // 新增主表
    @RequestMapping(value = "/addMain")
    @OperationVerify(OperationVerifyEnum.ADD)
    @ResponseBody
    public Map<String, Object> addMain(BillWmPlan billWmPlan,
	    HttpServletRequest req) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    HttpSession session = req.getSession();
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    Date date=new Date();
	    billWmPlan.setCreatetm(date);
	    billWmPlan.setCreator(user.getLoginName());
	    billWmPlan.setCreatorName(user.getUsername());
	    billWmPlan.setEditor(user.getLoginName());
	    billWmPlan.setEditorName(user.getUsername());
	    billWmPlan.setEdittm(date);
	    billWmPlan.setLocno(user.getLocNo());
	    billWmPlanManager.saveMain(billWmPlan);
	    map.put("result", ResultEnums.SUCCESS.getResultMsg());
	    map.put("planNo", billWmPlan.getPlanNo());
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", e.getMessage());
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", "系统异常");
	}
	return map;
    }

    // 新增主表
    @RequestMapping(value = "/eidtMain")
    @OperationVerify(OperationVerifyEnum.MODIFY)
    @ResponseBody
    public Map<String, Object> eidtMain(BillWmPlan billWmPlan,
	    HttpServletRequest req) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    HttpSession session = req.getSession();
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billWmPlan.setEdittm(new Date());
	    billWmPlan.setEditor(user.getLoginName());
	    billWmPlan.setEditorName(user.getUsername());
	    billWmPlan.setLocno(user.getLocNo());
	    billWmPlanManager.saveMain(billWmPlan);
	    map.put("result", ResultEnums.SUCCESS.getResultMsg());
	    map.put("planNo", billWmPlan.getPlanNo());
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", e.getMessage());
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", "系统异常");
	}
	return map;
    }

    // 删除
    @RequestMapping(value = "/deletePlan")
    @OperationVerify(OperationVerifyEnum.REMOVE)
    @ResponseBody
    public Map<String, Object> deletePlan(String keyStr, HttpSession session) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billWmPlanManager.deletePlan(keyStr, user.getLocNo());
	    map.put("result", ResultEnums.SUCCESS.getResultMsg());
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", e.getMessage());
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", "系统异常");
	}
	return map;
    }

    // 审核
    @RequestMapping(value = "/auditPlan")
    @OperationVerify(OperationVerifyEnum.VERIFY)
    @ResponseBody
    public Map<String, Object> auditPlan(String keyStr, HttpSession session) {
	Map<String, Object> map = new HashMap<String, Object>();
	try {
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billWmPlanManager.auditPlan(keyStr, user.getLocNo(),
		    user.getLoginName(),user.getUsername());
	    map.put("result", ResultEnums.SUCCESS.getResultMsg());
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", e.getMessage());
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    map.put("result", ResultEnums.FAIL.getResultMsg());
	    map.put("msg", "审核失败，请联系管理员");
	}
	return map;
    }
    
    
    /**
     * 转库存锁定
     * @param req
     * @param BillConStorelock
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ManagerException
     */
	@RequestMapping(value = "/toStoreLock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> toStoreLock(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
    	    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			ObjectMapper mapper = new ObjectMapper();
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillWmPlan> listWmPlans = new ArrayList<BillWmPlan>();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>(){});
				listWmPlans=convertListWithTypeReference(mapper,list);
			}
			billWmPlanManager.toStoreLock(listWmPlans,user);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
   			log.error("转库存锁定异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillWmPlan> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillWmPlan> tl=new ArrayList<BillWmPlan>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillWmPlan type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillWmPlan.class);
			tl.add(type);
		}
		return tl;
	}
	
	/**
	 * 转退厂申请
	 * @param billConStorelockKey
	 * @return
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/changeWMRequest")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> changeWMRequest(BillWmPlanKey billWmPlanKey,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		HttpSession session = req.getSession();
    	    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    		
			String locno=billWmPlanKey.getLocno();
    		String ownerNo=billWmPlanKey.getOwnerNo();
    		String planNo=billWmPlanKey.getPlanNo();        		
    		if(StringUtils.isEmpty(locno)){
    			flag.put("result", "fail");
    			flag.put("msg", "仓库编码不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		}        		
    		if(StringUtils.isEmpty(ownerNo)){
    			flag.put("result", "fail");
    			flag.put("msg", "货主编号不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		}        		
    		if(StringUtils.isEmpty(planNo)){
    			flag.put("result", "fail");
    			flag.put("msg", "计划编号不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		} 
    		
    		billWmPlanManager.changeWMRequest(billWmPlanKey,user.getLoginName(),user.getUsername());
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	/**
	 * 转移库计划
	 * @param billConStorelockKey
	 * @return
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/changeHMPlan")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> changeHMPlan(BillWmPlanKey billWmPlanKey,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		HttpSession session = req.getSession();
    	    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    		
			String locno=billWmPlanKey.getLocno();
    		String ownerNo=billWmPlanKey.getOwnerNo();
    		String planNo=billWmPlanKey.getPlanNo();        		
    		if(StringUtils.isEmpty(locno)){
    			flag.put("result", "fail");
    			flag.put("msg", "仓库编码不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		}        		
    		if(StringUtils.isEmpty(ownerNo)){
    			flag.put("result", "fail");
    			flag.put("msg", "货主编号不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		}        		
    		if(StringUtils.isEmpty(planNo)){
    			flag.put("result", "fail");
    			flag.put("msg", "计划编号不能为空。");
    			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
    		} 
    		
    		billWmPlanManager.changeHMPlan(billWmPlanKey,user.getLoginName(),user.getUsername());
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
}