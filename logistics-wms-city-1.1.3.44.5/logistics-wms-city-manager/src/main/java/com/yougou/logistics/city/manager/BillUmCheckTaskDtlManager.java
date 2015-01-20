package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
public interface BillUmCheckTaskDtlManager extends BaseCrudManager {
	
	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 查询退仓验收明细的所有店退仓单
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmCheckTaskDtl> findUntreadNo4CheckTaskDtl(Map<String, Object> params) throws ManagerException;
	
	/**
	 * 查询商品
	 * @param item
	 * @param page
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<Item> findItem4CheckTask(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 查询商品分页数量
	 * @param item
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findItemCount4CheckTask(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
	
	/**
	 * 保存退仓验收任务明细
	 * @param insertList
	 * @param updateList
	 * @param deleteList
	 * @param check
	 * @throws ServiceException
	 */
	public void saveUmCheckTaskDtl(List<BillUmCheckTaskDtl> insertList, List<BillUmCheckTaskDtl> updateList,
			List<BillUmCheckTaskDtl> deleteList, BillUmCheckTask check) throws ManagerException;
	
	/**
	 * 按计划保存
	 * @param params
	 * @throws DaoException
	 */
	public void saveCheckQty4itemQty(Map<String, Object> params) throws ManagerException;
	
	/**
	 * 按单删除操作
	 * @param taskDtlList
	 * @throws ServiceException
	 */
	public void delUntreadByCheckTask(List<BillUmCheckTaskDtl> taskDtlList) throws ManagerException;
	
	/**
	 * 商品置0
	 * @param taskDtlList
	 * @throws ServiceException
	 */
	public void updateCheckQtyToZero(List<BillUmCheckTaskDtl> taskDtlList) throws ManagerException;
	
}