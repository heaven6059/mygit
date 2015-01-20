package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BillUmLoadboxService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmLoadboxManager")
class BillUmLoadboxManagerImpl extends BaseCrudManagerImpl implements BillUmLoadboxManager {
	@Resource
	private BillUmLoadboxService billUmLoadboxService;

	@Override
	public BaseCrudService init() {
		return billUmLoadboxService;
	}

	@Override
	public void createLoadBox(String strKey, BillUmUntreadMm untreadMm, SystemUser user) throws ManagerException {
		try {
			billUmLoadboxService.createLoadBox(strKey, untreadMm, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void auditLoadBox(String keyStr, String locno, String oper) throws ManagerException {
		try {
			billUmLoadboxService.auditLoadBox(keyStr, locno, oper);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}

	}

}