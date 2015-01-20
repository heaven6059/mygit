package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.BillChReportMapper;

@Service("billChReportService")
class BillChReportServiceImpl extends BaseCrudServiceImpl implements BillChReportService {
    @Resource
    private BillChReportMapper billChReportMapper;

    @Override
    public BaseCrudMapper init() {
        return billChReportMapper;
    }

	@Override
	public Map<String, Integer> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChReportMapper.selectSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}