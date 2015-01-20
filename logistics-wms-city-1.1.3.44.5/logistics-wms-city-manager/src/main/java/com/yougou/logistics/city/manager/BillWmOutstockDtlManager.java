package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
public interface BillWmOutstockDtlManager extends BaseCrudManager {
	
	/**
	 * 选择退厂通知单带出明细
	 * @param authorityParams TODO
	 * @param billWmRecedeDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDtlDto> findOutstockDtlItem(BillWmOutstockDtl billWmOutstockDtl, AuthorityParams authorityParams) throws ManagerException;
	/**
	 * 批量打印退厂拣货明细
	 * @param locno
	 * @param keys
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public List<String> printDetail(String locno,String keys,SystemUser user)throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException;
	
	
	/**
	 * 选择退厂通知单带出明细分页
	 * @param authorityParams TODO
	 * @param billWmRecedeDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDtlDto> findOutstockDtlItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	public int findOutstockDtlItemCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
	
	public SumUtilMap<String, Object> selectOutstockDtlItemSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException;	
	
}