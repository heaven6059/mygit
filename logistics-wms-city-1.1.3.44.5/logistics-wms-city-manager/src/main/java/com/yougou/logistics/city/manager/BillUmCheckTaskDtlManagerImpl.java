package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmCheckTaskDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
@Service("billUmCheckTaskDtlManager")
class BillUmCheckTaskDtlManagerImpl extends BaseCrudManagerImpl implements BillUmCheckTaskDtlManager {
	@Resource
	private BillUmCheckTaskDtlService billUmCheckTaskDtlService;

	@Override
	public BaseCrudService init() {
		return billUmCheckTaskDtlService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckTaskDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillUmCheckTaskDtl> findUntreadNo4CheckTaskDtl(Map<String, Object> params) throws ManagerException {
		try {
			return billUmCheckTaskDtlService.findUntreadNo4CheckTaskDtl(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<Item> findItem4CheckTask(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckTaskDtlService.findItem4CheckTask(params, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findItemCount4CheckTask(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckTaskDtlService.findItemCount4CheckTask(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void saveUmCheckTaskDtl(List<BillUmCheckTaskDtl> insertList, List<BillUmCheckTaskDtl> updateList,
			List<BillUmCheckTaskDtl> deleteList, BillUmCheckTask check) throws ManagerException {
		try {
			billUmCheckTaskDtlService.saveUmCheckTaskDtl(insertList, updateList, deleteList, check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void saveCheckQty4itemQty(Map<String, Object> params) throws ManagerException {
		try {
			billUmCheckTaskDtlService.saveCheckQty4itemQty(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void delUntreadByCheckTask(List<BillUmCheckTaskDtl> taskDtlList) throws ManagerException {
		try {
			billUmCheckTaskDtlService.delUntreadByCheckTask(taskDtlList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void updateCheckQtyToZero(List<BillUmCheckTaskDtl> taskDtlList) throws ManagerException {
		try {
			billUmCheckTaskDtlService.updateCheckQtyToZero(taskDtlList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}