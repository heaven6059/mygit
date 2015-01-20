package com.yougou.logistics.city.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmUntreadMmManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
@RequestMapping("/bill_um_untread_mm")
@ModuleVerify("25060040")
public class BillUmUntreadMmController extends BaseCrudController<BillUmUntreadMm> {
	@Log
	private Logger log;
	@Resource
	private BillUmUntreadMmManager billUmUntreadMmManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billumuntreadmm/", billUmUntreadMmManager);
	}

	//新增主表
	@RequestMapping(value = "/addMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> addMain(BillUmUntreadMm untreadMm, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			untreadMm.setCreatetm(new Date());
			untreadMm.setCreator(user.getLoginName());
			untreadMm.setLocno(user.getLocNo());
			billUmUntreadMmManager.saveMain(untreadMm);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("untreadMmNo", untreadMm.getUntreadMmNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	//新增主表
	@RequestMapping(value = "/eidtMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> eidtMain(BillUmUntreadMm untreadMm, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			untreadMm.setEdittm(new Date());
			untreadMm.setEditor(user.getLoginName());
			untreadMm.setLocno(user.getLocNo());
			billUmUntreadMmManager.saveMain(untreadMm);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("untreadMmNo", untreadMm.getUntreadMmNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	//删除
	@RequestMapping(value = "/deleteUntreadMm")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteUntreadMm(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadMmManager.deleteUntread(keyStr, user.getLocNo());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	//审核
	@RequestMapping(value = "/auditUntreadMm")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditUntreadMm(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadMmManager.auditUntread(keyStr, user.getLocNo(), user.getLoginName());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg","审核失败，请联系管理员");
		}
		return map;
	}
}