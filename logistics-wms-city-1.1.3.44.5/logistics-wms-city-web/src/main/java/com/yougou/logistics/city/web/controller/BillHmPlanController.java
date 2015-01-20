package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillHmPlanDtlManager;
import com.yougou.logistics.city.manager.BillHmPlanManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@RequestMapping("/bill_hm_plan")
@ModuleVerify("25100010")
public class BillHmPlanController<ModelType> extends BaseCrudController<BillHmPlan> {

	@Log
	private Logger log;

	@Resource
	private BillHmPlanManager billHmPlanManager;
	
	@Resource
	private BillHmPlanDtlManager billHmPlanDtlManager;


	@Override
	public CrudInfo init() {
		return new CrudInfo("billhmplan/", billHmPlanManager);
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
			int total = this.billHmPlanManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillHmPlan> list = this.billHmPlanManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
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

	/**
	 * 删除退仓收货单
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/delBillHmPlan")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String,Object> delBillHmPlan(HttpServletRequest req) throws ManagerException {

        Map<String,Object> result = new HashMap<String,Object>();
		try {

			List<BillHmPlan> oList = null;
			ObjectMapper mapper = new ObjectMapper();
			Map<CommonOperatorEnum, List<BillHmPlan>> params = new HashMap<CommonOperatorEnum, List<BillHmPlan>>();
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");

			if (StringUtils.isNotBlank(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}

			boolean isSuccess = false;
			if (CommonUtil.hasValue(oList)) {
				isSuccess = billHmPlanManager.deleteBillHmPlan(oList);
			}

			if (isSuccess) {
				result.put("flag", "success");
			} else {
				result.put("flag", "fail");
				result.put("msg", "删除失败！");
			}

		} catch (ManagerException e) {
			log.error("=======删除移库单异常：" + e.getMessage(), e);
			result.put("flag", "fail");
			result.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error("=======删除移库单异常：" + e.getMessage(), e);
			result.put("flag", "fail");
			result.put("msg", e.getMessage());
		}

		return result;
	}

	/**
	 * 新增单据
	 * @param billHmPlan
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/saveMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveMain(BillHmPlan billHmPlan, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String planNo = billHmPlanManager.saveMain(billHmPlan, user);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			result.put("planNo", planNo);
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("planNo", ResultEnums.FAIL.getResultMsg());
			return result;
		}
	}
	
	/**
	 * 修改单据
	 * @param billHmPlan
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/editMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> editMain(BillHmPlan billHmPlan, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String planNo = billHmPlanManager.saveMain(billHmPlan, user);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			result.put("planNo", planNo);
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("planNo", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
			return result;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<ModelType> tl = new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/audit")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> audit(String planNo, HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billHmPlanManager.audit(planNo, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/checkCellNo4BillHmPlan")
	@ResponseBody
	public Map<String, Object> checkCellNo4BillHmPlan(HttpServletRequest req, String cellNo ,String planNo, HttpSession session)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);						
			obj=billHmPlanDtlManager.checkDtlCell(planNo, user.getLocNo(), cellNo);			
			return obj;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", e.getMessage());
			return obj;
		}
	}
	
	/**
	 * 作废移库单
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/cancelBillHmPlan")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> cancelBillHmPlan(HttpServletRequest req) throws ManagerException { 
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillHmPlan> oList = null;
			ObjectMapper mapper = new ObjectMapper();
			Map<CommonOperatorEnum, List<BillHmPlan>> params = new HashMap<CommonOperatorEnum, List<BillHmPlan>>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			if (StringUtils.isNotBlank(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billHmPlanManager.cancelBillHmPlan(oList, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
}