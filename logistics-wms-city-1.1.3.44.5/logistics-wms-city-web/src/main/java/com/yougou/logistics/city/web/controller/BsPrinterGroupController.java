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
import com.yougou.logistics.city.common.model.BsPrinterGroup;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BsPrinterGroupManager;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 10:16:07 CST 2013
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
@RequestMapping("/bs_printer_group")
@ModuleVerify("25030210")
public class BsPrinterGroupController extends BaseCrudController<BsPrinterGroup> {
	
	@Log
	private Logger log;
	
    @Resource
    private BsPrinterGroupManager bsPrinterGroupManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bsprintergroup/",bsPrinterGroupManager);
    }
    @RequestMapping(value = "/savebsprintergroup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String savebsprintergroup(String printerGroupNo,String printerNoStr, HttpServletRequest req) {
		if (StringUtils.isBlank(printerGroupNo)) {
			return "fail";
		}
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bsPrinterGroupManager.savePrinterGroup(printerGroupNo,printerNoStr, user.getLoginName(), user.getLocNo());
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
}