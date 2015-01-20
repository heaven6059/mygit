package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillConStoreLockQuery;

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
public interface BillConStorelockDtlService extends BaseCrudService {
	
	/**
	 * 新增明细
	 * @param storelock
	 */
	public void saveStorelockDtl(BillConStoreLockQuery lockQuery) throws ServiceException;
	
	/**
	 * 删除明细
	 * @param storelock
	 */
	public void delStorelockDtl(BillConStoreLockQuery lockQuery) throws ServiceException;
	
	
	/**
	 * 查询客户库存锁定分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findConContentGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询客户库存锁定分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	
	
	/**
	 * 查询客户库存锁定数据
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> findConContentGroup(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查询客户库存锁定明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findStorelockGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询客户库存锁定明细分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> findStorelockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查找可转退厂申请的锁定库存明细
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillConStorelockDtl> find4WmPlan(Map<String,Object> params) throws ServiceException;

	List<BillConStorelockDtl> findStorelockGroup(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException;
	
}