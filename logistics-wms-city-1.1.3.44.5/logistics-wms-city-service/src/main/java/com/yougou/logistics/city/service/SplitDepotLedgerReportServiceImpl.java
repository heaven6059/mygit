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
import com.yougou.logistics.city.dal.mapper.SplitDepotLedgerReportMapper;

/**
 * 
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-23 上午10:37:25
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("splitDepotLedgerReportService")
public class SplitDepotLedgerReportServiceImpl extends BaseCrudServiceImpl implements SplitDepotLedgerReportService {

	@Resource
	private SplitDepotLedgerReportMapper splitDepotLedgerReportMapper;

	@Override
	public BaseCrudMapper init() {
		return splitDepotLedgerReportMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return splitDepotLedgerReportMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}
