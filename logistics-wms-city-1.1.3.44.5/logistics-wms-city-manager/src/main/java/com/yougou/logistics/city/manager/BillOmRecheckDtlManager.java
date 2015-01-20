package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货复核单明细manager
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:20:37
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillOmRecheckDtlManager extends BaseCrudManager {

	/**
	 * 复核明细汇总分页查询
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmRecheckDtl> findBillOmRecheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 复核明细汇总查询总数
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findBillOmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 根据箱号，商品编码分组查询
	 * 
	 * @param dtl
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtl> findGroupByBoxAndItem(BillOmRecheckDtl dtl) throws ManagerException;

	/**
	 * 
	 * @param dtl
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtl> findSizeNoDetail(BillOmRecheckDtl dtl) throws ManagerException;

	/**
	 * 根据箱号打印
	 * 
	 * @param locno
	 * @param recheckNo
	 * @param boxNo
	 * @param noneDtl
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> printByBox(String locno, String recheckNo, String boxNo, boolean noneDtl, SystemUser user)
			throws ManagerException;

	/**
	 * 打印明细(显示外箱号)
	 * 
	 * @param keys
	 *            locno|recheckNo|storeNo
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> printDetailShowBox(String keys, SystemUser user) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	public SumUtilMap<String, Object> selectRfSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 打印
	 * 
	 * @param recheck
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNo(BillOmRecheck recheck) throws ManagerException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtl4Print(BillOmRecheckDtlSizeKind dtl) throws ManagerException;

	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNoAndBox(BillOmRecheck recheck) throws ManagerException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtlBox4Print(BillOmRecheckDtlSizeKind recheck)
			throws ManagerException;

	public List<String> selectAllDtlSizeKind(BillOmRecheck recheck) throws ManagerException;
	
	/**
	 * 更新复核明细
	 * @param recheck
	 * @param checkList
	 * @throws ServiceException
	 */
	public void updateOmRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> updateList,SystemUser user) throws ManagerException;

	/**
	 * 删除复核明细
	 * @param recheck
	 * @param updateList
	 * @throws ServiceException
	 */
	public void deleteOmRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> deleteList,SystemUser user) throws ManagerException;
	
	/**
	 * 更新拣货复核明细
	 * @param recheck
	 * @param checkList
	 * @throws ServiceException
	 */
	public void updateOmOutstockRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> updateList,List<BillOmRecheckDtl> insertList,SystemUser user) throws ManagerException;
	
	/**
	 * 删除拣货复核明细
	 * @param recheck
	 * @param updateList
	 * @throws ServiceException
	 */
	public void deleteOmOutstockRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> deleteList,SystemUser user) throws ManagerException;

	/**
	 * 复核明细汇总分页查询根据箱号汇总
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmRecheckDtl> findRecheckDtlGroupByBoxPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 复核明细汇总查询总数箱号汇总
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findRecheckDtlGroupByBoxCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
}