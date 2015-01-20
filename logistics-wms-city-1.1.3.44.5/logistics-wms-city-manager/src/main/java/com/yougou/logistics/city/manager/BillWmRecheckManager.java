package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.Supplier;

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
public interface BillWmRecheckManager extends BaseCrudManager {
	
	public int countLabelNoByRecheckNo(BillWmRecheck cc, AuthorityParams authorityParams) throws ManagerException;
	
	public List<BillWmRecheck> findLabelNoByRecheckNoPage(SimplePage page, BillWmRecheck cc, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 查询退长通知单里面的供应商
	 * @param locno
	 * @return
	 */
	public List<Supplier> querySupplier(String locno)throws ManagerException;

	public List<BillWmOutstockDtl> queryRecheckItem(Map<String, Object> params)throws ManagerException;

	public void packageBox(List<BillWmOutstockDtlDto> dtlLst, String boxNo,
			String recheckNo, String locno, String supplierNo, String userName,String userChName,
			AuthorityParams authorityParams) throws ManagerException;

	public void check(String ids, String loginName, String checkUser)throws ManagerException;
	
	/**
	 * 删除拣货复核单
	 * @param listOmRechecks
	 * @throws ManagerException
	 */
	public void deleteBillWmOutStockRecheck(List<BillWmRecheck> listWmRechecks)throws ManagerException;
	
	/**
	 * RF复核装箱
	 * @param billWmRecheck
	 * @throws ServiceException
	 */
	public void packageBoxRf(BillWmRecheck billWmRecheck) throws ManagerException;
	
}