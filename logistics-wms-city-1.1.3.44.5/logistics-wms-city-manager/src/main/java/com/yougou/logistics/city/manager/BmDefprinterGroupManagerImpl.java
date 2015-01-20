package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefprinterGroup;
import com.yougou.logistics.city.service.BmDefprinterGroupService;

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
@Service("bmDefprinterGroupManager")
class BmDefprinterGroupManagerImpl extends BaseCrudManagerImpl implements BmDefprinterGroupManager {
	@Resource
	private BmDefprinterGroupService bmDefprinterGroupService;

	@Override
	public BaseCrudService init() {
		return bmDefprinterGroupService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteGroupBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BmDefprinterGroup group = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					group = new BmDefprinterGroup();
					group.setLocno(substr[0]);
					group.setPrinterGroupNo(substr[1]);
					count += bmDefprinterGroupService.deleteById(group);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}
}