package com.yougou.logistics.city.service;

import java.util.Date;
import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;

/**
 * TODO: 业务单据汇总查询
 * 
 * @author ye.kl
 * @date 2014-1-23 下午7:42:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillAggregateService extends BaseCrudService {
	/**
	 * 查询存在异常的单据商品数量
	 * 
	 * @param zoneNo
	 * @param locNo
	 * @param beginDate
	 * @param endDate
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<BillUnusualDto> findBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate,SimplePage page) throws ServiceException;
	public List<BillUnusualDto> findBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate) throws ServiceException;
	/**
	 * 查询存在异常的单据记录数
	 * 
	 * @param zoneNo
	 * @param locNo
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	public int findBillUnusualCount(String zoneNo, String locNo, Date beginDate, Date endDate) throws ServiceException;

}
