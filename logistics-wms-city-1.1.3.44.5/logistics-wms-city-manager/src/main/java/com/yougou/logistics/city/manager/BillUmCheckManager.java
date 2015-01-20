package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
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
public interface BillUmCheckManager extends BaseCrudManager {

	/**
	 * 删除退仓验收单
	 * @param listBillUmChecks
	 * @return
	 * @throws ManagerException
	 */
	public int deleteBillUmCheck(List<BillUmCheck> listBillUmChecks) throws ManagerException;

	/**
	 * 审核退仓收货单存储过程
	 * @param map
	 * @throws DaoException
	 */
	public Map<String, Object> procBillUmCheckAuditQuery(List<BillUmCheck> listBillUmChecks) throws ManagerException;

	public int selectCountUmNoForInstock(Map map) throws ManagerException;

	/**
	 * 保存主档
	 * @param insertList TODO
	 * @param updateList TODO
	 * @param deleteList TODO
	 * @param untreadMm
	 */
	public void saveMain(BillUmCheck check, List<BillUmCheckDtl> insertList, List<BillUmCheckDtl> updateList, List<BillUmCheckDtl> deleteList) throws ManagerException;

	public void auditCheck(String keyStr, SystemUser user) throws ManagerException;

	public List<BillUmCheck> selectByPageUmNoForInstock(SimplePage page, Map map) throws ManagerException;

	public int selectCountCheckNoForInstock(Map map, AuthorityParams authorityParams) throws ManagerException;

	public List<BillUmCheck> selectByPageCheckNoForInstock(SimplePage page, Map map, AuthorityParams authorityParams) throws ManagerException;

	public int selectCount4loadBox(Map map, AuthorityParams authorityParams) throws ManagerException;

	public List<BillUmCheck> select4loadBoxByPage(Map map, SimplePage page, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectUntreadJoinCheckDtlSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectUntreadJoinCheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	
	/**
	 * 转货操作
	 * @param map key:checkNos（退仓验收单号逗号隔开）、locno仓别
	 * @return
	 * @author wanghb
	 */
	public Map<String, Object> transferCargo(Map<String, Object> map,SystemUser currentUser)throws ManagerException; 
	
	
	public Map<String, Object> validateIsEnable(String checkNoStr, String locno,String ownerNo)throws ManagerException; 

	
	public int findBillUmCheckCount(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException;

	public List<BillUmCheck> findBillUmCheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 批量门店转货
	 * @param checkList
	 * @param storeList
	 * @param user TODO
	 * @throws ManagerException
	 */
	public void toStoreConvertRecheck(List<BillUmCheck> checkList,List<Store> storeList, SystemUser user) throws ManagerException;
	
	public Map<String, Object> prpertyChange(Map<String, Object> map,SystemUser currentUser)throws ManagerException;

}