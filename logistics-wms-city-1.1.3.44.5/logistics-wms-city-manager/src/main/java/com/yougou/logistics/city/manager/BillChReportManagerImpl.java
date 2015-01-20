package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillChReportService;

@Service("billChReportManager")
class BillChReportManagerImpl extends BaseCrudManagerImpl implements BillChReportManager {
    @Resource
    private BillChReportService billChReportService;
    @Override
    public BaseCrudService init() {
        return billChReportService;
    }
	@Override
	public Map<String, Integer> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billChReportService.findSumQty(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

}