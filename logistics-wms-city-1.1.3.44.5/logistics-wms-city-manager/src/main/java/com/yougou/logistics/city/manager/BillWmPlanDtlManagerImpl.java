package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.service.BillWmPlanDtlService;

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
@Service("billWmPlanDtlManager")
class BillWmPlanDtlManagerImpl extends BaseCrudManagerImpl implements
	BillWmPlanDtlManager {
    @Resource
    private BillWmPlanDtlService billWmPlanDtlService;

    @Override
    public BaseCrudService init() {
	return billWmPlanDtlService;
    }

    @Override
    public List<Item> selectItem(SimplePage page, Map<String, Object> map,AuthorityParams authorityParams)
	    throws ManagerException {
	try {
	    return billWmPlanDtlService.selectItem(page, map,authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException();
	}
    }

    @Override
    public int selectItemCount(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billWmPlanDtlService.selectItemCount(map,authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException();
	}
    }

    public void saveDetail(String insertStrs, String deleteStrs,
	    BillWmPlan plan) throws ManagerException {
	try {
	    billWmPlanDtlService.saveDetail(insertStrs, deleteStrs, plan);
	} catch (ServiceException e) {
	    throw new ManagerException(e.getMessage());
	}
    }
}