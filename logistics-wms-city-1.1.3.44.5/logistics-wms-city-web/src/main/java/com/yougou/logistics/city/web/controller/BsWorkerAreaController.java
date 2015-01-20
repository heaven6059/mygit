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
import com.yougou.logistics.city.common.model.BsWorkerArea;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BsWorkerAreaManager;

/*
 * 人员区域关系维护
 * @author luo.hl
 * @date  Wed Sep 25 14:31:21 CST 2013
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
@RequestMapping("/bs_worker_area")
@ModuleVerify("25030230")
public class BsWorkerAreaController extends BaseCrudController<BsWorkerArea> {
	@Log
	private Logger log;
	@Resource
	private BsWorkerAreaManager bsWorkerAreaManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bsworkerarea/", bsWorkerAreaManager);
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(BsWorkerArea BsWorkerArea, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			BsWorkerArea.setLocno(user.getLocNo());
			BsWorkerArea area = bsWorkerAreaManager.findById(BsWorkerArea);
			if (null != area) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/addBsWorkerArea")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addOsDefline(BsWorkerArea area, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			area.setCreatetm(new Date());
			area.setCreator(user.getLoginName());
			area.setLocno(user.getLocNo());
			bsWorkerAreaManager.add(area);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editBsWorkerArea")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String editBsWorkerArea(BsWorkerArea area, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			area.setEdittm(new Date());
			area.setEditor(user.getLoginName());
			area.setLocno(user.getLocNo());
			if (bsWorkerAreaManager.modifyById(area) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteBsWorkerArea")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteBsWorkerAreaManager(String keyStr, HttpServletRequest req) {
		try {
			if (bsWorkerAreaManager.deleteBsWorkerAreaBatch(keyStr) > 0) {
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