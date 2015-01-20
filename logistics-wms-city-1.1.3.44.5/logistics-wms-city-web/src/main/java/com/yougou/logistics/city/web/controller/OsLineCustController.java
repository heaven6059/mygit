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
import com.yougou.logistics.city.common.model.OsLineCust;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.OsLineCustManager;

/*
 * 客户线路关系维护
 * @author luo.hl
 * @date  Thu Sep 26 12:33:06 CST 2013
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
@RequestMapping("/os_line_cust")
public class OsLineCustController extends BaseCrudController<OsLineCust> {
	@Log
	private Logger log;

	@Resource
	private OsLineCustManager osLineCustManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("oslinecust/", osLineCustManager);
	}

	@RequestMapping(value = "/addOsLineCust")
	@ResponseBody
	public String addOsLineCust(OsLineCust lineCust, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			lineCust.setCreatetm(new Date());
			lineCust.setCreator(user.getLoginName());
			lineCust.setLocno(user.getLocNo());
			osLineCustManager.add(lineCust);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(OsLineCust lineCust, HttpServletRequest req) {
		try {
			OsLineCust type = osLineCustManager.findById(lineCust);
			if (null != type) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editOsLineCust")
	@ResponseBody
	public String editOsLineCust(OsLineCust lineCust, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			lineCust.setEdittm(new Date());
			lineCust.setEditor(user.getLoginName());
			lineCust.setLocno(user.getLocNo());
			if (osLineCustManager.modifyById(lineCust) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteOsLineCust")
	@ResponseBody
	public String deleteOsLineCust(String keyStr, HttpServletRequest req) {
		try {
			if (osLineCustManager.deleteOsLineCustBatch(keyStr) > 0) {
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