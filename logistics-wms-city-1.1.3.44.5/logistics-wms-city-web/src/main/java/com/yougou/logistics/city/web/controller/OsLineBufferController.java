package com.yougou.logistics.city.web.controller;

import java.util.Date;
import java.util.List;

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
import com.yougou.logistics.city.common.model.OsLineBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.OsLineBufferManager;

/*
 * 线路暂存区维护
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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
@RequestMapping("/os_line_buffer")
public class OsLineBufferController extends BaseCrudController<OsLineBuffer> {
	@Log
	private Logger log;
	@Resource
	private OsLineBufferManager osLineBufferManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("oslinebuffer/", osLineBufferManager);
	}

	@RequestMapping(value = "/addOsLineBuffer")
	@ResponseBody
	public String addOsLineBuffer(OsLineBuffer line, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setCreatetm(new Date());
			line.setLocno(user.getLocNo());
			line.setCreator(user.getLoginName());
			osLineBufferManager.add(line);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(OsLineBuffer line, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setLocno(user.getLocNo());
			OsLineBuffer curLine = osLineBufferManager.findById(line);
			if (null != curLine) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editOsLineBuffer")
	@ResponseBody
	public String editOsLineBuffer(OsLineBuffer line, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setEdittm(new Date());
			line.setEditor(user.getLoginName());
			line.setLocno(user.getLocNo());
			if (osLineBufferManager.modifyById(line) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteOsLineBuffer")
	@ResponseBody
	public String deleteOsLineBuffer(String keyStr, HttpServletRequest req) {
		try {
			if (osLineBufferManager.deleteOsLineBufferBatch(keyStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
	@RequestMapping(value = "/getStoreInfo")
	@ResponseBody
	public List<Store> getStoreInfo(String locno,String cellNo) {
		try {
			return osLineBufferManager.getStoreInfo(cellNo,locno);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
}