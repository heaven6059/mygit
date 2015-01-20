package com.yougou.logistics.city.web.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.OsDefline;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.OsDeflineManager;

/*
 * 基本线路维护
 * @author luo.hl
 * @date  Wed Sep 25 14:33:44 CST 2013
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
@RequestMapping("/os_defline")
public class OsDeflineController extends BaseCrudController<OsDefline> {
	@Log
	private Logger log;

	@Resource
	private OsDeflineManager osDeflineManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("osdefline/", osDeflineManager);
	}

	@RequestMapping(value = "/addOsDefline")
	@ResponseBody
	public String addOsDefline(OsDefline line, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setCreatetm(new Date());
			line.setCreator(user.getLoginName());
			line.setLocno(user.getLocNo());
			osDeflineManager.add(line);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(OsDefline line, HttpServletRequest req) {
		try {
			OsDefline curLine = osDeflineManager.findById(line);
			if (null != curLine) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editOsDefline")
	@ResponseBody
	public String editOsDefline(OsDefline line, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setEdittm(new Date());
			line.setEditor(user.getLoginName());
			line.setLocno(user.getLocNo());
			if (osDeflineManager.modifyById(line) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteOsDefline")
	@ResponseBody
	public String deleteOsDefline(String keyStr, HttpServletRequest req) {
		try {
			if (osDeflineManager.deleteOsDeflineBatch(keyStr) > 0) {
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