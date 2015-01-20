package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillOmOutstockManager extends BaseCrudManager {
	
	/**
	 * 拣货确认
	 * @param locno
	 * @param outstockNo
	 * @param keyStr
	 * @throws ManagerException
	 */
	public void checkOutstock(String locno, String outstockNo, String outstockName, String keyStr,SystemUser user)
			throws ManagerException;
	
	/**
	 * 发单
	 * @param locno
	 * @param assignNameCh TODO
	 * @param keyStr
	 * @param outstockNo
	 * @throws ManagerException
	 */
	public void sendOrder(String locno, String assignName, String assignNameCh, String keyStr,SystemUser user  )
			throws ManagerException;
	
	
	/**
	 * 查询即时移库分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findMoveStockCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 确认即时移库
	 * @param listOutstocks
	 * @throws ServiceException
	 */
	public void queryBill(List<BillOmOutstock> listOutstocks, SystemUser user) throws ManagerException;
	
	/**
	 * 审核
	 * @param username TODO
	 * @param outstock
	 * @throws ManagerException
	 */
	public void auditOutstock(List<BillOmOutstock> oList,String loginName, String username) throws ManagerException;
	
	public SumUtilMap<String, Object> selectImmediateMoveSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}