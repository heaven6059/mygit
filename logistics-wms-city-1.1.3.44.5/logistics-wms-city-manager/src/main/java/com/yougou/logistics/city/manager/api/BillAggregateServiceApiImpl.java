package com.yougou.logistics.city.manager.api;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.BillAggregateServiceApi;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;
import com.yougou.logistics.city.service.BillAggregateService;

/**
 * TODO: 业务单据汇总查询实现类
 * 
 * @author ye.kl
 * @date 2014-1-23 下午12:14:36
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billAggregateServiceApi")
public class BillAggregateServiceApiImpl implements BillAggregateServiceApi{
	@Resource
    private BillAggregateService billAggregateService;
	
	@Override
	public List<BillUnusualDto> getBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate,SimplePage page)
			throws RpcException {
		try {
			Assert.hasText(zoneNo,"区域编码不能为空");
			Assert.notNull(beginDate,"开始日期不能为空");
			Assert.notNull(endDate,"结束日期不能为空");
			return billAggregateService.findBillUnusual(zoneNo, locNo, beginDate, endDate, page);
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}

	@Override
	public int getBillUnusualCount(String zoneNo, String locNo, Date beginDate, Date endDate) throws RpcException {
		try {
			Assert.hasText(zoneNo,"区域编码不能为空");
			Assert.notNull(beginDate,"开始日期不能为空");
			Assert.notNull(endDate,"结束日期不能为空");
			return billAggregateService.findBillUnusualCount(zoneNo, locNo, beginDate, endDate);
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}

	@Override
	public List<BillUnusualDto> getBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate)
			throws RpcException {
		try {
			Assert.hasText(zoneNo,"区域编码不能为空");
			Assert.notNull(beginDate,"开始日期不能为空");
			Assert.notNull(endDate,"结束日期不能为空");
			return billAggregateService.findBillUnusual(zoneNo, locNo, beginDate, endDate);
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}
}


