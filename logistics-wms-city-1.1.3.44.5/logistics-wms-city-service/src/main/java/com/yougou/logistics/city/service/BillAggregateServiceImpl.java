package com.yougou.logistics.city.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.dal.database.BillAggregateMapper;

/**
 * TODO: 业务单据汇总查询
 * 
 * @author ye.kl
 * @date 2014-1-23 下午7:43:12
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billAggregateService")
public class BillAggregateServiceImpl extends BaseCrudServiceImpl implements BillAggregateService {
	@Resource
	private BillAggregateMapper billAggregateMapper;

	@Override
	public BaseCrudMapper init() {
		return billAggregateMapper;
	}

	@Override
	public List<BillUnusualDto> findBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate,SimplePage page)
			throws ServiceException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("zoneNo", zoneNo);
			params.put("locNo", locNo);
			params.put("beginDate", DateUtil.format1(beginDate));
			params.put("endDate", DateUtil.format1(endDate));
			return billAggregateMapper.selectBillUnusualByPage(params,page);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int findBillUnusualCount(String zoneNo, String locNo, Date beginDate, Date endDate) throws ServiceException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("zoneNo", zoneNo);
			params.put("locNo", locNo);
			params.put("beginDate", DateUtil.format1(beginDate));
			params.put("endDate", DateUtil.format1(endDate));
			return billAggregateMapper.selectBillUnusualCount(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public List<BillUnusualDto> findBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate)
			throws ServiceException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("zoneNo", zoneNo);
			params.put("locNo", locNo);
			params.put("beginDate", DateUtil.format1(beginDate));
			params.put("endDate", DateUtil.format1(endDate));
			return billAggregateMapper.selectBillUnusual(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
