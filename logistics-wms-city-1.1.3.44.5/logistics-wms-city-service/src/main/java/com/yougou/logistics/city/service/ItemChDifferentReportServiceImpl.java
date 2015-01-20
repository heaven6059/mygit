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
import com.yougou.logistics.city.dal.mapper.ItemChDifferentReportMapper;

@Service("itemChDifferentReportService")
public class ItemChDifferentReportServiceImpl extends BaseCrudServiceImpl implements ItemChDifferentReportService {
	
	@Resource
	private ItemChDifferentReportMapper itemChDifferentReportMapper;
	
	@Override
	public BaseCrudMapper init() {
		return itemChDifferentReportMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return itemChDifferentReportMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
