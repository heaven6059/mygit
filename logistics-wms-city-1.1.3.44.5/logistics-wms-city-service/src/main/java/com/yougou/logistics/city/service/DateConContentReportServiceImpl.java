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
import com.yougou.logistics.city.dal.mapper.DateConContentReportMapper;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-2-22 下午7:00:35
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("dateConContentReportService")
public class DateConContentReportServiceImpl extends BaseCrudServiceImpl implements DateConContentReportService {
	
	@Resource
	private DateConContentReportMapper dateConContentReportMapper;

	@Override
	public BaseCrudMapper init() {
		return dateConContentReportMapper;
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return dateConContentReportMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
