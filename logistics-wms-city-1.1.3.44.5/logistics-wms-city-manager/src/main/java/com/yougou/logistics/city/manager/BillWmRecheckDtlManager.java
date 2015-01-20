package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
public interface BillWmRecheckDtlManager extends BaseCrudManager {
	
	public Map<String, Object> printByBox(String locno, BillWmRecheck billWmRecheck, String boxKeys,boolean noneDtl) throws ManagerException;
	
	/**
	 * 查询退厂复核单数量
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findWmRecheckDtlCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 查询退厂复核单分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillWmRecheckDtl> findWmRecheckDtlByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;

	/**
	 * 查询退厂复核单箱明细汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findWmRecheckDtlGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 查询退厂复核单箱明细汇总分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillWmRecheckDtl> findWmRecheckDtlGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	
	public SumUtilMap<String, Object> selectWmRecheckDtlSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
}