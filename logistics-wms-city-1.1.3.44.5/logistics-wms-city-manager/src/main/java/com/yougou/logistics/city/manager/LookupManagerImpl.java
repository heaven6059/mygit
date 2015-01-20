package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.LookupService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lookupManager")
class LookupManagerImpl extends BaseCrudManagerImpl implements LookupManager {
    @Resource
    private LookupService lookupService;

    @Override
    public BaseCrudService init() {
        return lookupService;
    }

	@Override
	public int checkItemValue(String itemval, String lookupcode, int systemid){
		try {
			return lookupService.checkItemValue(itemval, lookupcode, systemid);
		} catch (ServiceException e) {
//			new ManagerException(e);
			return 1;
		}
	}

	@Override
	@Transactional
	public void deletelookup(String lookupcode){
		try {
			lookupService.deletelookup(lookupcode);
		} catch (ServiceException e) {
			new ManagerException(e);
		}
	}

	@Override
	public int checkLookuoCode(String lookupcode) throws ManagerException {
		try {
			return lookupService.checkLookuoCode(lookupcode);
		} catch (ServiceException e) {
//			new ManagerException(e);
			return 1;
		}
	}
}