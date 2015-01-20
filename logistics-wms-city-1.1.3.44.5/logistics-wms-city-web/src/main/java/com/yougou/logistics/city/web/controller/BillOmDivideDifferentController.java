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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentKey;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmDivideDifferentManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
@RequestMapping("/bill_om_divide_different")
public class BillOmDivideDifferentController extends BaseCrudController<BillOmDivideDifferent> {
	
	@Log
	private Logger log;
	
	@Resource
	private BillOmDivideDifferentManager billOmDivideDifferentManager;
	
	private static final String STATUS10 = "10";

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomdividedifferent/", billOmDivideDifferentManager);
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
			int total = billOmDivideDifferentManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDivideDifferent> list = billOmDivideDifferentManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
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
	
	@RequestMapping(value = "/editMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> editMain(BillOmDivideDifferent different, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BillOmDivideDifferentKey differentKey = new BillOmDivideDifferentKey();
			differentKey.setLocno(different.getLocno());
			differentKey.setDifferentNo(different.getDifferentNo());
			BillOmDivideDifferent entity=(BillOmDivideDifferent) billOmDivideDifferentManager.findById(differentKey);
			if(entity==null||!STATUS10.equals(entity.getStatus())){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据: " + different.getDifferentNo() +"已删除或状态已改变!");
				return map;
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			different.setEditor(user.getLoginName());
			different.setEditorName(user.getUsername());
			different.setEdittm(new Date());
			billOmDivideDifferentManager.modifyById(different);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	// 转商品差异调整
	@RequestMapping(value = "/toDivideDifferent")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> toDivideDifferent(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
 		try {
 			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillOmDivideDtl> divideDtlList = new ArrayList<BillOmDivideDtl>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				divideDtlList = convertListWithTypeReferenceDivide(mapper, list);
			}
			HttpSession session = req.getSession();
 			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmDivideDifferentManager.toDivideDifferent(divideDtlList, user.getLoginName(),user.getUsername());
 			map.put("result", ResultEnums.SUCCESS.getResultMsg());
 		} catch (ManagerException e) {
 			log.error(e.getMessage(), e);
 			map.put("result", ResultEnums.FAIL.getResultMsg());
 			map.put("msg", e.getMessage());
 		} catch (Exception e) {
 			log.error(e.getMessage(), e);
 			map.put("result", ResultEnums.FAIL.getResultMsg());
 			map.put("msg", "操作失败,请联系管理员!");
 		}
 		return map;
	}
	
	// 删除差异调整单
	@RequestMapping(value = "/delDivideDifferent")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> delDivideDifferent(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillOmDivideDifferent> divideDtlList = new ArrayList<BillOmDivideDifferent>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				divideDtlList = convertListWithTypeReference(mapper, list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmDivideDifferentManager.delDivideDifferent(divideDtlList, user.getLoginName());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "删除失败,请联系管理员!");
		}
		return map;
	}
	
	// 审核差异调整单
	@RequestMapping(value = "/auditDivideDifferent")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditDivideDifferent(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillOmDivideDifferent> divideDtlList = new ArrayList<BillOmDivideDifferent>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				divideDtlList = convertListWithTypeReference(mapper, list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmDivideDifferentManager.auditDivideDifferent(divideDtlList, user.getLoginName(),user.getUsername());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "审核失败,请联系管理员!");
		}
		return map;
	}
		
	
	@SuppressWarnings("rawtypes")
	private List<BillOmDivideDtl> convertListWithTypeReferenceDivide(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmDivideDtl> tl = new ArrayList<BillOmDivideDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivideDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmDivideDtl.class);
			tl.add(type);
		}
		return tl;
	}
	
	@SuppressWarnings("rawtypes")
	private List<BillOmDivideDifferent> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmDivideDifferent> tl = new ArrayList<BillOmDivideDifferent>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivideDifferent type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmDivideDifferent.class);
			tl.add(type);
		}
		return tl;
	}
}