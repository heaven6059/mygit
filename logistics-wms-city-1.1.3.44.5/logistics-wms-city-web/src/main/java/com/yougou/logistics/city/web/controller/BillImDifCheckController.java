package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.manager.BillImCheckManager;

/*
 * 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@RequestMapping("/bill_im_difcheck")
@ModuleVerify("25070090")
public class BillImDifCheckController extends BaseCrudController<BillImCheck> {
	@Log
	private Logger log;
	@Resource
	private BillImCheckManager billImCheckManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billimdifcheck/", billImCheckManager);
	}

}