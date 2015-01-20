package com.yougou.logistics.city.web.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefprinterGroup;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmDefprinterGroupManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Nov 01 11:21:40 CST 2013
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
@RequestMapping("/bm_defprinter_group")
@ModuleVerify("25030200")
public class BmDefprinterGroupController extends BaseCrudController<BmDefprinterGroup> {
	@Log
	private Logger log;
	@Resource
	private BmDefprinterGroupManager bmDefprinterGroupManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bmdefprintergroup/", bmDefprinterGroupManager);
	}

	@RequestMapping(value = "/addGroup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addGroup(BmDefprinterGroup group, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			group.setCreatetm(new Date());
			group.setCreator(user.getLoginName());
			group.setLocno(user.getLocNo());
			bmDefprinterGroupManager.add(group);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(HttpServletRequest req, Model model) {
		try {
			Map param = this.builderParams(req, model);
			int count = bmDefprinterGroupManager.findCount(param);
			if (count != 0) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editGroup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String editGroup(BmDefprinterGroup group, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			group.setEdittm(new Date());
			group.setEditor(user.getLoginName());
			group.setLocno(user.getLocNo());
			if (bmDefprinterGroupManager.modifyById(group) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteGroup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteGroup(String keyStr, HttpServletRequest req) {
		try {
			if (bmDefprinterGroupManager.deleteGroupBatch(keyStr) > 0) {
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