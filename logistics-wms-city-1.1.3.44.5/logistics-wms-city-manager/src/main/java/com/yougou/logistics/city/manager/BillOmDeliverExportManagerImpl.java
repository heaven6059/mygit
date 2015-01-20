package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmDeliverExportService;

/**
 * 
 * 装车单详情manager
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:26:33
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDeliverExportManager")
class BillOmDeliverExportManagerImpl extends BaseCrudManagerImpl implements BillOmDeliverExportManager {
    @Resource
    private BillOmDeliverExportService billOmDeliverExportService;

    @Override
    public BaseCrudService init() {
        return billOmDeliverExportService;
    }

	@Override
	public List<Map<String, Object>> findDeliverDtlSize(Map<String,Object> params) throws ManagerException {
		try {
			return billOmDeliverExportService.findDeliverDtlSize(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Map<String, Object>> findDeliverDtlSizeNum(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverExportService.findDeliverDtlSizeNum(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<String> findAllDtlSizeKind(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmDeliverExportService.findAllDtlSizeKind(params, authorityParams);
	}
	
	@Override
	public Map<String, Object> findBillOmDeliverExportByPage(BillOmDeliverExport rep,
			AuthorityParams authorityParams, boolean all) throws ManagerException {
		try {
			return this.billOmDeliverExportService.findBillOmDeliverExportByPage(rep, authorityParams, all);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmDeliverExportService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}