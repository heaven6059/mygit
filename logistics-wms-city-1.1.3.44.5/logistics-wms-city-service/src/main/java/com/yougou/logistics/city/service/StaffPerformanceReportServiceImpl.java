package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.StaffPerformanceReportMapper;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-7-1 上午9:57:03
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("staffPerformanceReportService")
public class StaffPerformanceReportServiceImpl extends BaseCrudServiceImpl implements StaffPerformanceReportService {
	
	@Resource
	private StaffPerformanceReportMapper staffPerformanceReportMapper;
	
	@Override
	public BaseCrudMapper init() {
		return staffPerformanceReportMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return staffPerformanceReportMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}
