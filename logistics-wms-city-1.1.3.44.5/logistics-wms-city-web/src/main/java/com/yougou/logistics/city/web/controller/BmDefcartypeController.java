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
import com.yougou.logistics.city.common.model.BmDefcartype;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmDefcartypeManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 09:27:35 CST 2013
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
@RequestMapping("/bm_defcartype")
public class BmDefcartypeController extends BaseCrudController<BmDefcartype> {
	@Log
	private Logger log;

	@Resource
	private BmDefcartypeManager bmDefcartypeManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bmdefcartype/", bmDefcartypeManager);
	}

	@RequestMapping(value = "/addDefcartype")
	@ResponseBody
	public String addDefcartype(BmDefcartype bmDefcartype, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefcartype.setCreatetm(new Date());
			bmDefcartype.setCreator(user.getLoginName());
			bmDefcartypeManager.add(bmDefcartype);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(BmDefcartype bmDefcartype, HttpServletRequest req) {
		try {
			BmDefcartype type = bmDefcartypeManager.findById(bmDefcartype);
			if (null != type) {
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
	public String editDefdock(BmDefcartype bmDefcartype, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefcartype.setEdittm(new Date());
			bmDefcartype.setEditor(user.getLoginName());
			if (bmDefcartypeManager.modifyById(bmDefcartype) > 0) {
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
	public String deleteDefdock(String keyStr, HttpServletRequest req) {
		try {
			if (bmDefcartypeManager.deleteBmDefcartypeBatch(keyStr) > 0) {
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