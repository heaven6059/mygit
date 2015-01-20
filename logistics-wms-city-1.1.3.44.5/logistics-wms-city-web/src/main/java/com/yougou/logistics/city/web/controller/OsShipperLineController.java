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
import com.yougou.logistics.city.common.model.OsShipperLine;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.OsShipperLineManager;

/*
 * 线路承运商关系维护
 * @author luo.hl
 * @date  Thu Sep 26 11:04:15 CST 2013
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
@RequestMapping("/os_shipper_line")
public class OsShipperLineController extends BaseCrudController<OsShipperLine> {
	@Log
	private Logger log;

	@Resource
	private OsShipperLineManager osShipperLineManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("osshipperline/", osShipperLineManager);
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(OsShipperLine line, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setLocno(user.getLocNo());
			OsShipperLine curLine = osShipperLineManager.findById(line);
			if (null != curLine) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/addOsShipperLine")
	@ResponseBody
	public String addOsShipperLine(OsShipperLine line, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setCreatetm(new Date());
			line.setCreator(user.getLoginName());
			line.setLocno(user.getLocNo());
			osShipperLineManager.add(line);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editOsShipperLine")
	@ResponseBody
	public String editOsShipperLine(OsShipperLine line, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setLocno(user.getLocNo());
			line.setEdittm(new Date());
			line.setEditor(user.getLoginName());
			if (osShipperLineManager.modifyById(line) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteOsShipperLine")
	@ResponseBody
	public String OsShipperLine(String keyStr, HttpServletRequest req) {
		try {
			if (osShipperLineManager.deleteOsShipperLineBatch(keyStr) > 0) {
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