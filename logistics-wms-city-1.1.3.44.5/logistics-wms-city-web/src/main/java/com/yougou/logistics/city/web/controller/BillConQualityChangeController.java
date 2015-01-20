package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillConQualityChange;
import com.yougou.logistics.city.manager.BillConQualityChangeManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 24 13:54:46 CST 2013
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
@RequestMapping("/bill_con_quality_change")
public class BillConQualityChangeController extends BaseCrudController<BillConQualityChange> {
	@Resource
	private BillConQualityChangeManager billConQualityChangeManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billconqualitychange/", billConQualityChangeManager);
	}
}