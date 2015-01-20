package com.yougou.logistics.city.web.controller;

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
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmCheckManager;
import com.yougou.logistics.city.manager.BillUmLoadboxManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
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
@RequestMapping("/bill_um_loadbox")
@ModuleVerify("25060100")
public class BillUmLoadboxController extends BaseCrudController<BillUmLoadbox> {
	@Log
	private Logger log;
	@Resource
	private BillUmLoadboxManager billUmLoadboxManager;

	@Resource
	private BillUmCheckManager billUmCheckManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billumloadbox/", billUmLoadboxManager);
	}

	@RequestMapping(value = "/mainlist.json")
	@ResponseBody
	public Map<String, Object> mainlist(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billUmLoadboxManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImInstock> list = this.billUmLoadboxManager.findByPage(page, sortColumn, sortOrder, params,
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
	
	@RequestMapping(value = "/selectCheck4LoadBox")
	@ResponseBody
	public Map<String, Object> selectCheck4LoadBox(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();

		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    	int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
//			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
//			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
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
			
			String statusStr = (String) params.get("status");
			if (StringUtils.isNotEmpty(statusStr)) {
				params.put("status", statusStr.split(","));	
			}			
			
			int total = billUmCheckManager.selectCount4loadBox(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillUmCheck> list = billUmCheckManager.select4loadBoxByPage(params, page, authorityParams);

			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/createLoadBox")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> createLoadBox(BillUmUntreadMm untreadMm, HttpServletRequest req, String keyStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
//			untreadMm.setCreatetm(new Date());
//			untreadMm.setCreator(user.getLoginName());
			untreadMm.setLocno(user.getLocNo());
			billUmLoadboxManager.createLoadBox(keyStr, untreadMm, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "生成任务失败，请联系管理员");
		}
		return map;
	}

	@RequestMapping(value = "/auditLoadBox")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditLoadBox(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmLoadboxManager.auditLoadBox(keyStr, user.getLocNo(), user.getLoginName());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}
}