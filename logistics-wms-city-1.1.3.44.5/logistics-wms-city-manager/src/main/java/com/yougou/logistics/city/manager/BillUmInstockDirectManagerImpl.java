package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.BillUmInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmInstockDirectService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Nov 18 12:08:45 CST 2013
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
@Service("billUmInstockDirectManager")
class BillUmInstockDirectManagerImpl extends BaseCrudManagerImpl implements BillUmInstockDirectManager {
	@Resource
	private BillUmInstockDirectService billUmInstockDirectService;

	@Resource
	private ProcCommonService procCommonService;

	@Log
	private Logger logger;

	@Override
	public BaseCrudService init() {
		return billUmInstockDirectService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void createTask(String locNo, String ownerNo, String sourceNo, String keyStr, String loginName)
			throws ManagerException {
		try {
			procCommonService.createTask(locNo, ownerNo, sourceNo, keyStr, loginName);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void sendOrder(String locNo, String ownerNo, String sender, String keyStr, String loginName)
			throws ManagerException {
		try {
			procCommonService.sendOrder(locNo, ownerNo, sender, keyStr, loginName);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmInstockDirectService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void akeySendOrder(BillUmInstockDirect instockDirect) throws ManagerException {
		try {
			billUmInstockDirectService.akeySendOrder(instockDirect);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}