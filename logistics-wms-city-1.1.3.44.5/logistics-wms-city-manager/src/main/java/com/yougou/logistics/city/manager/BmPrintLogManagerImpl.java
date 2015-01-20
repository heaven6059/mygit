package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BmPrintLogService;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
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
@Service("bmPrintLogManager")
class BmPrintLogManagerImpl extends BaseCrudManagerImpl implements BmPrintLogManager {
	@Resource
	private BmPrintLogService bmPrintLogService;

	@Override
	public BaseCrudService init() {
		return bmPrintLogService;
	}

	@Override
	public List<String> getLabelPrefix(SystemUser user, int qty, String printType, String storeName)
			throws ManagerException {
		try {
			return bmPrintLogService.getLabelPrefix(user, qty, printType, storeName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public Object getLabel4Full(SystemUser user, String printType,
			String dataStr) throws ManagerException {
		try {
			return bmPrintLogService.getLabel4Full(user, printType, dataStr);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}