package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.CsInstockSettingdtlService;

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
@Service("csInstockSettingdtlManager")
class CsInstockSettingdtlManagerImpl extends BaseCrudManagerImpl implements CsInstockSettingdtlManager {
	@Resource
	private CsInstockSettingdtlService csInstockSettingdtlService;

	@Override
	public BaseCrudService init() {
		return csInstockSettingdtlService;
	}

}