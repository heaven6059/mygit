package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BillWmPlanService;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
@Service("billWmPlanManager")
class BillWmPlanManagerImpl extends BaseCrudManagerImpl implements
	BillWmPlanManager {
    @Resource
    private BillWmPlanService billWmPlanService;

    @Override
    public BaseCrudService init() {
	return billWmPlanService;
    }

    @Override
    public void saveMain(BillWmPlan billWmPlan) throws ManagerException {
	try {
	    billWmPlanService.saveMain(billWmPlan);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}

    }

    @Override
    public void deletePlan(String keyStr, String locnoNo)
	    throws ManagerException {
	try {
	    billWmPlanService.deletePlan(keyStr, locnoNo);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public void auditPlan(String keyStr, String locno, String oper,String userName)
	    throws ManagerException {
	try {
	    billWmPlanService.auditPlan(keyStr, locno, oper,userName);
	} catch (ServiceException e) {
	    throw new ManagerException(e.getMessage());
	}
    }

	@Override
	public void toStoreLock(List<BillWmPlan> list, SystemUser user) throws ManagerException {
		try {
			billWmPlanService.toStoreLock(list,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void changeWMRequest(BillWmPlanKey billWmPlanKey,String userName,String chUserName)
			throws ManagerException {
		try {
			billWmPlanService.changeWMRequest(billWmPlanKey,userName,chUserName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}
	@Override
	public void changeHMPlan(BillWmPlanKey billWmPlanKey,String userName,String chUserName)
			throws ManagerException {
		try {
			billWmPlanService.changeHMPlan(billWmPlanKey,userName,chUserName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}
    
}