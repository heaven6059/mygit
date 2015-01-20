package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CsCellPackSetting;
import com.yougou.logistics.city.service.CsCellPackSettingService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Oct 09 18:46:25 CST 2013
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
@Service("csCellPackSettingManager")
class CsCellPackSettingManagerImpl extends BaseCrudManagerImpl implements CsCellPackSettingManager {
	@Resource
	private CsCellPackSettingService csCellPackSettingService;

	@Override
	public BaseCrudService init() {
		return csCellPackSettingService;
	}

	@Override
	public int deleteCsCellPackSetting(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			CsCellPackSetting setting = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					setting = new CsCellPackSetting();
					setting.setLocno(substr[0]);
					setting.setPackSpec(substr[1]);
					setting.setAreaType(substr[2]);
					count += csCellPackSettingService.deleteById(setting);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

}