package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmDivideDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billOmDivideDtlManager")
class BillOmDivideDtlManagerImpl extends BaseCrudManagerImpl implements BillOmDivideDtlManager {
    @Resource
    private BillOmDivideDtlService billOmDivideDtlService;

    @Override
    public BaseCrudService init() {
        return billOmDivideDtlService;
    }

	@Override
	public int modifyBillOmDivideByDivideNoAndlocno(BillOmDivide divide)
			throws ManagerException {
		try {
			return this.billOmDivideDtlService.modifyBillOmDivideByDivideNoAndlocno(divide);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDivideDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}