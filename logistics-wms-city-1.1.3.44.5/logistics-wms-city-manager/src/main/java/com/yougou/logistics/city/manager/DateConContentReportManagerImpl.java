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
import com.yougou.logistics.city.service.DateConContentReportService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-2-22 下午7:02:14
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("dateConContentReportManager")
public class DateConContentReportManagerImpl extends BaseCrudManagerImpl implements DateConContentReportManager {
	
	@Resource
	private DateConContentReportService dateConContentReportService;

	@Override
	protected BaseCrudService init() {
		return dateConContentReportService;
	}
	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return dateConContentReportService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}
