package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * 
 * 收货验收单manager
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:14:21
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillImCheckManager extends BaseCrudManager {

	public void add(BillImCheck billImCheck, List<Item> itemLst) throws ManagerException;

	public Map<String, Object> update(BillImCheck billImCheck, List<BillImCheckDtl> insertItemLst,
			List<BillImCheckDtl> updatedItemLst, List<BillImCheckDtl> deletedItemLst) throws ManagerException;

	public int deleteBatch(String ids) throws ManagerException;

	public void check(List<BillImCheck> checkList, String userId,String username) throws ManagerException;

	public int findCheckForDirectCount(Map map, AuthorityParams authorityParams) throws ManagerException;

	public List<BillImCheck> selectByPageForDirect(SimplePage page, Map map, AuthorityParams authorityParams) throws ManagerException;

	public void addMain(BillImCheck billImCheck, List<BillImCheckDtl> insertItemLst,
			List<BillImCheckDtl> updatedItemLst, List<BillImCheckDtl> deletedItemLst) throws ManagerException;
	
	public void addMain(BillImCheck billImCheck,AuthorityParams authorityParams) throws ManagerException;

	public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
	
	public Map<String, Object> editMainInfo(BillImCheck billImCheck,String currUser) throws ManagerException;
	/**
	 * 收货单直接转验收单
	 * @param nos 收货单号,用逗号隔开
	 * @param locno
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public  Map<String, Object> directCheck(String nos,String locno,SystemUser user,AuthorityParams authorityParams) throws ManagerException;
}