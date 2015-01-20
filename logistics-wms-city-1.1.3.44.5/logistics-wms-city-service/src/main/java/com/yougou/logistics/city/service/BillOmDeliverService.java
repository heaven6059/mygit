package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverKey;

/**
 * 
 * 装车单service
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:27:00
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverService extends BaseCrudService {
	
	public void checkBillOmDeliver(Map<String, String> map) throws ServiceException;
	
	public int deleteByDeliverDtl(BillOmDeliverKey key) throws ServiceException;
	
	public List<BillOmDeliver> selectLoadproposeDtl(BillOmDeliver billOmDeliver) throws ServiceException;
	
	public int loadproposeDtlCount(BillOmDeliver billOmDeliver) throws ServiceException;
	
	public int updateLoadproposeDtl(BillOmDeliver billOmDeliver) throws ServiceException;
	
	public int updateLoadpropose(BillOmDeliver billOmDeliver) throws ServiceException;
	
	public int updateBillOmDeliverDtl(BillOmDeliver billOmDeliver) throws ServiceException;
	
	public List<BillOmDeliver> findBillOmDeliverSum(Map<String,Object> params,AuthorityParams authorityParams)
    	    throws ServiceException;

	public List<BillOmDeliver> findPrintOmDeliverList(String sortColumn,
			String sortOrder, Map<String, Object> params,
			AuthorityParams authorityParams)throws ServiceException;
	
}