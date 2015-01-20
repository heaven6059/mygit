package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
public interface BillWmOutstockDirectService extends BaseCrudService {
	
	/**
	 * 退厂拣货下架指示表发单
	 * @param listDirects
	 * @throws ServiceException
	 */
	public void sendWmOutstockDirect(BillWmOutstock billWmOutstock,List<BillWmOutstockDirect> listDirects) throws ServiceException;
	
	/**
	 * 更新下架指示表
	 * @param direct
	 * @return
	 * @throws DaoException
	 */
	public int updateBillWmOutstockDirect(BillWmOutstockDirect direct) throws ServiceException; 
	
	/**
	 * 查询是否全部拣货完成
	 * @param direct
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDirect> findBillWmOutstockDirectAndOutstockDtl(Map<String, Object> maps) throws ServiceException; 
	
}