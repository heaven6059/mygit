package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillConStorelockDtlManager extends BaseCrudManager {
	
	/**
	 * 新增主表
	 * @param storelock
	 */
	public void saveStorelockDtl(BillConStoreLockQuery lockQuery) throws ManagerException;
	
	
	/**
	 * 删除明细
	 * @param storelock
	 */
	public void delStorelockDtl(BillConStoreLockQuery lockQuery) throws ManagerException;
	
	
	/**
	 * 查询客户库存锁定分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findConContentGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	
	/**
	 * 查询客户库存锁定数据
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> findConContentGroup(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
	
	/**
	 * 查询客户库存锁定明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findStorelockGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	public Map<String,Object> importStorelockDtlExcel(List<BillConStorelockDtl> list,AuthorityParams authorityParams ,Map<String, Object> params)throws ManagerException;
}