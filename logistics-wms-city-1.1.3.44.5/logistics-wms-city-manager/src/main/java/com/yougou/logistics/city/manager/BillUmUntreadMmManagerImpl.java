package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.service.BillUmUntreadMmService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
@Service("billUmUntreadMmManager")
class BillUmUntreadMmManagerImpl extends BaseCrudManagerImpl implements BillUmUntreadMmManager {
	@Resource
	private BillUmUntreadMmService billUmUntreadMmService;

	@Override
	public BaseCrudService init() {
		return billUmUntreadMmService;
	}

	@Override
	public void saveMain(BillUmUntreadMm untreadMm) throws ManagerException {
		try {
			billUmUntreadMmService.saveMain(untreadMm);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}

	}

	@Override
	public void deleteUntread(String keyStr, String locnoNo) throws ManagerException {
		try {
			billUmUntreadMmService.deleteUntreadMm(keyStr, locnoNo);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void auditUntread(String keyStr, String locno, String oper) throws ManagerException {
		try {
			billUmUntreadMmService.auditUntreadMm(keyStr, locno, oper);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}