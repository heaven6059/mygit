package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BsPrinterDock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BsPrinterDockManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Nov 01 15:18:23 CST 2013
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
@RequestMapping("/bs_printer_dock")
@ModuleVerify("25030220")
public class BsPrinterDockController extends BaseCrudController<BsPrinterDock> {
	@Log
	private Logger log;

	@Resource
	private BsPrinterDockManager bsPrinterDockManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bsprinterdock/", bsPrinterDockManager);
	}

	@RequestMapping(value = "/saveBsPrinterDock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveBsPrinterDock(String paramStr, HttpServletRequest req) {
		if (StringUtils.isBlank(paramStr)) {
			return "fail";
		}
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bsPrinterDockManager.saveWorkerLoc(paramStr, user.getLoginName(), user.getLocNo());
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
}