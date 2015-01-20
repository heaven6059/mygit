package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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
public interface BillOmOutstockService extends BaseCrudService {

	/**
	 * 查询即时移库分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findMoveStockCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询即时移库分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmOutstock> findMoveStockByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 确认即时移库
	 * @param listOutstocks
	 * @throws ServiceException
	 */
	public void queryBill(List<BillOmOutstock> listOutstocks, SystemUser user) throws ServiceException;
	
	
	public SumUtilMap<String, Object> selectImmediateMoveSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}