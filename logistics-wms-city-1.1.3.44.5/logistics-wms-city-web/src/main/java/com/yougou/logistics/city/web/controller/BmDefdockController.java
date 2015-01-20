package com.yougou.logistics.city.web.controller;

import java.util.Date;

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
import com.yougou.logistics.city.common.model.BmDefdock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmDefdockManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 23 10:24:36 CST 2013
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
@RequestMapping("/bm_defdock")
@ModuleVerify("25030180")
public class BmDefdockController extends BaseCrudController<BmDefdock> {
	@Log
	private Logger log;

	@Resource
	private BmDefdockManager bmDefdockManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bmdefdock/", bmDefdockManager);
	}

	@RequestMapping(value = "/addDefdock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addDefdock(BmDefdock bmDefdock, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefdock.setCreatetm(new Date());
			bmDefdock.setCreator(user.getLoginName());
			bmDefdock.setLocno(user.getLocNo());
			bmDefdockManager.add(bmDefdock);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(BmDefdock bmDefdock, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefdock.setLocno(user.getLocNo());
			BmDefdock curDock = bmDefdockManager.findById(bmDefdock);
			if (null != curDock) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editDefdock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String editDefdock(BmDefdock bmDefdock, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefdock.setEdittm(new Date());
			bmDefdock.setEditor(user.getLoginName());
			bmDefdock.setLocno(user.getLocNo());
			if (bmDefdockManager.modifyById(bmDefdock) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteDefdock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteDefdock(String keyStr, HttpServletRequest req) {
		try {
			if (bmDefdockManager.deleteBmDefdockBatch(keyStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

}