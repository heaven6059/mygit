package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConStorelock;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillConStorelockService extends BaseCrudService {
	
	/**
	 * 审核
	 * @param storelock
	 */
	public void auditStorelock(List<BillConStorelock> lists) throws ServiceException;
	
	/**
	 * 删除
	 * @param storelock
	 */
	public void delStorelock(List<BillConStorelock> lists) throws ServiceException;
	
	/**
	 * 手工关闭
	 * @param lockQuery
	 * @throws ServiceException
	 */
	public void overStoreLock(List<BillConStorelock> lists) throws ServiceException;
	
	/**
	 * 转退厂申请
	 * @param storelock
	 * @throws ServiceException
	 */
	public void toWmRequest(BillConStorelock storelock) throws ServiceException;

	/**
	 * 库存冻结单合计
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams)throws ServiceException;
	
}