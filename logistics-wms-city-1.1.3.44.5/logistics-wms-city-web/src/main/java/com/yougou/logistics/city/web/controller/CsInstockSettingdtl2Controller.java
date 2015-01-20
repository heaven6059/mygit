package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl2;
import com.yougou.logistics.city.manager.CsInstockSettingdtl2Manager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 09:58:17 CST 2013
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
@RequestMapping("/cs_instock_settingdtl2")
public class CsInstockSettingdtl2Controller extends BaseCrudController<CsInstockSettingdtl2> {
	@Resource
	private CsInstockSettingdtl2Manager csInstockSettingdtl2Manager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("csinstocksettingdtl2/", csInstockSettingdtl2Manager);
	}
}