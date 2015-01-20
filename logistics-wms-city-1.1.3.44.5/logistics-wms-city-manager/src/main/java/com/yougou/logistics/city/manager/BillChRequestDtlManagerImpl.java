package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChRequestDtl;
import com.yougou.logistics.city.service.BillChRequestDtlService;

@Service("billChRequestDtlManager")
class BillChRequestDtlManagerImpl extends BaseCrudManagerImpl implements BillChRequestDtlManager {
    @Resource
    private BillChRequestDtlService billChRequestDtlService;

    @Override
    public BaseCrudService init() {
        return billChRequestDtlService;
    }

	@Override
	public int findCountForJoinItem(Map<String, Object> params)
			throws ManagerException {
		try {
			return this.billChRequestDtlService.findCountForJoinItem(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillChRequestDtl> findForJoinItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params) throws ManagerException {
		try {
			return billChRequestDtlService.findForJoinItemByPage(page, orderByField, orderBy, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}