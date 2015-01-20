package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货复核单manager
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:20:04
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmRecheckManager extends BaseCrudManager {
	
	/**
	 * 分货单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public List<BillOmDivide> selectDivideCollectByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 汇总总数
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public int selectDivideCollectCount(Map<String,Object> params, AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 查询dtl表中的客户
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ManagerException
	 */
	public List<Store> selectStoreByParam(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询分货单中需要复核的商品信息
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillOmDivideDtl> queryRecheckBoxItem(Map<String, Object> params)throws ManagerException;

	public void packageBox(List<BillOmDivideDtl> dtlLst, BillOmRecheck billOmRecheck, String boxNo)throws ManagerException;

	public void check(String ids, SystemUser user, String checkUser)throws ManagerException;
	
	/**
	 * 审核拣货复核单
	 * @param ids
	 * @param checkUserName
	 * @param checkUser
	 * @throws ManagerException
	 */
	public void checkOutStock(String ids, SystemUser user, String checkUser)throws ManagerException;
	
	/**
	 * 删除复核表
	 * @param listOmRechecks
	 * @throws ManagerException
	 */
	public void deleteBillOmRecheck(List<BillOmRecheck> listOmRechecks)throws ManagerException;
	
	/**
	 * 删除拣货复核单
	 * @param listOmRechecks
	 * @throws ManagerException
	 */
	public void deleteBillOmOutStockRecheck(List<BillOmRecheck> listOmRechecks)throws ManagerException;
	
	/**
	 * 拣货复核装箱
	 * @param dtlLst
	 * @param billOmRecheck
	 * @param boxNo
	 * @throws ManagerException
	 */
	public void packageBoxOutstock(List<BillOmOutstockDtlDto> dtlLst, BillOmRecheck billOmRecheck, String boxNo)throws ManagerException;
	
	/**
	 * RF封箱
	 * @param billOmRecheck
	 * @throws ServiceException
	 */
	public void packageBoxRf(BillOmRecheck billOmRecheck) throws ManagerException;
	
	/**
	 * RF拣货复核封箱
	 * @param billOmRecheck
	 * @throws ServiceException
	 */
	public void packageBoxOutstockRf(BillOmRecheck billOmRecheck) throws ManagerException;
	
	/**
	 * 分货单明细汇总查询总数
	 * @param params
	 * @param authorityParams TODO
	 * @param authorityParams
	 * @return
	 */
	public int findRecheckBoxItemCount(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 分货单明细汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams TODO
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmDivideDtl> findRecheckBoxItemByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectRecheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectOutstockRecheckSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 复核打包总页数
	 * @param billOmLocate
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findOutstockRecheckCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 复核打包信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billOmExp
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheck> findOutstockRecheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 查询来源单号总数量
	 * @param billOmLocate
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findCount4Source(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 分页查询来源单号
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billOmExp
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheck> findByPage4Source(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
}