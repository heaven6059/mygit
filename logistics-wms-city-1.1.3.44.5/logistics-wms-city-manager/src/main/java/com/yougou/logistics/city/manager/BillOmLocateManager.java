package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
public interface BillOmLocateManager extends BaseCrudManager {

	public int findBillOmLocateCount(BillOmLocate billOmLocate, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillOmLocate> findBillOmLocateByPage(SimplePage page, String orderByField, String orderBy,
			BillOmLocate billOmLocate, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 出库续调调用存储过程
	 * @param billOmExp
	 * @throws ServiceException
	 */
	public void procBillOmExpContinueDispatchQuery(List<BillOmLocate> listBillOmLocates, BillOmLocate billOmLocate)
			throws ManagerException;

	/**
	 * 手工完结波次单据
	 * @param billOmLocate
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, String> overBillOmLocate(List<BillOmLocate> lists,SystemUser user) throws ManagerException;

	/**
	 * 发单还原
	 * @param lists
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, String> recoveryLocateSend(List<BillOmLocate> lists) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(BillOmLocate billOmLocate, AuthorityParams authorityParams)
			throws ManagerException;
	
	/**
	 * 删除拣货波茨单
	 * @param lists
	 * @throws ServiceException
	 */
	public void deleteOmLocate(List<BillOmLocate> lists,String loginName) throws ManagerException;
}