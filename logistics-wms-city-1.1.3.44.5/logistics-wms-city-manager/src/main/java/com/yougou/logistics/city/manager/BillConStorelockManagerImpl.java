package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.service.BillConStorelockService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@Service("billConStorelockManager")
class BillConStorelockManagerImpl extends BaseCrudManagerImpl implements BillConStorelockManager {
	
    @Resource
    private BillConStorelockService billConStorelockService;

    @Override
    public BaseCrudService init() {
        return billConStorelockService;
    }

	@Override
	public void auditStorelock(List<BillConStorelock> lists) throws ManagerException {
		try {
			billConStorelockService.auditStorelock(lists);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void delStorelock(List<BillConStorelock> lists) throws ManagerException {
		try {
			billConStorelockService.delStorelock(lists);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void overStoreLock(List<BillConStorelock> lists) throws ManagerException {
		try {
			billConStorelockService.overStoreLock(lists);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void toWmRequest(BillConStorelock storelock) throws ManagerException {
		try {
			billConStorelockService.toWmRequest(storelock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockService.selectSumQty(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
}