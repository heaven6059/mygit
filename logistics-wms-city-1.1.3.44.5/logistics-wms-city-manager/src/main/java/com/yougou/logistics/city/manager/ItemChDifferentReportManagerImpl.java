package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.ItemChDifferentReportService;

@Service("itemChDifferentReportManager")
public class ItemChDifferentReportManagerImpl extends BaseCrudManagerImpl
		implements ItemChDifferentReportManager {

	@Resource
    private ItemChDifferentReportService itemChDifferentReportService;
    
	@Override
	protected BaseCrudService init() {
		return itemChDifferentReportService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return itemChDifferentReportService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}
