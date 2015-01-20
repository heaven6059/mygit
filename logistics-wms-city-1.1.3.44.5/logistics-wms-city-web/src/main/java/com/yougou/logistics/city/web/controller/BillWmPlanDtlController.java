package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmPlanDtlManager;
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
@RequestMapping("/bill_wm_plan_dtl")
public class BillWmPlanDtlController extends BaseCrudController<BillWmPlanDtl> {
    @Resource
    private BillWmPlanDtlManager billWmPlanDtlManager;
    @Log
    private Logger log;

    @Override
    public CrudInfo init() {
	return new CrudInfo("billwmplandtl/", billWmPlanDtlManager);
    }

    @RequestMapping(value = "/dtl_list.json")
    @ResponseBody
    public Map<String, Object> dtlList(HttpServletRequest req, Model model)
	    throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    Map<String, Object> params = builderParams(req, model);
	    int total = this.billWmPlanDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
	    SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
	    List<Item> list = this.billWmPlanDtlManager.findByPage(page, null,
		    null, params,authorityParams, DataAccessRuleEnum.BRAND);
	    obj.put("total", total);
	    obj.put("rows", list);
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	}
	return obj;
    }

    @RequestMapping(value = "/selectItem.json")
    @ResponseBody
    public Map<String, Object> queryList(HttpServletRequest req, Model model)
	    throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    Map<String, Object> params = builderParams(req, model);
	    int total = this.billWmPlanDtlManager.selectItemCount(params,authorityParams);
	    SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
	    List<Item> list = this.billWmPlanDtlManager
		    .selectItem(page, params,authorityParams);
	    obj.put("total", total);
	    obj.put("rows", list);
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	}
	return obj;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/saveDetail")
    @ResponseBody
    public Map<String, Object> saveDetail(HttpServletRequest req,
	    BillWmPlan plan, HttpSession session) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	try {
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    plan.setLocno(user.getLocNo());
	    String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? ""
		    : req.getParameter("inserted");
	    String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? ""
		    : req.getParameter("deleted");
	    billWmPlanDtlManager.saveDetail(inserted, deleted, plan);
	    resultMap.put("result", ResultEnums.SUCCESS.getResultMsg());
	    return resultMap;
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    resultMap.put("result", ResultEnums.FAIL.getResultMsg());
	    resultMap.put("msg", e.getMessage());
	    return resultMap;
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    resultMap.put("result", ResultEnums.FAIL.getResultMsg());
	    resultMap.put("msg", e.getMessage());
	    return resultMap;
	}
    }

    @SuppressWarnings("rawtypes")
    private List<Item> convertListWithTypeReference(ObjectMapper mapper,
	    List<Map> list) throws JsonParseException, JsonMappingException,
	    JsonGenerationException, IOException {
	List<Item> tl = new ArrayList<Item>(list.size());
	for (int i = 0; i < list.size(); i++) {
	    Item type = mapper.readValue(
		    mapper.writeValueAsString(list.get(i)), Item.class);
	    tl.add(type);
	}
	return tl;
    }
}