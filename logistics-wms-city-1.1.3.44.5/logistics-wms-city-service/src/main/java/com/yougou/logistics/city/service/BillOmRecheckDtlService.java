package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货复核单明细service
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:21:52
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillOmRecheckDtlService extends BaseCrudService {

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
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 复核明细汇总查询总数
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findBillOmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 根据箱号，商品编码分组查询
	 * 
	 * @param dtl
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtl> findGroupByBoxAndItem(BillOmRecheckDtl dtl) throws ServiceException;

	/**
	 * 
	 * @param dtl
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtl> findSizeNoDetail(BillOmRecheckDtl dtl) throws ServiceException;

	/**
	 * 查询复核明细关联外箱号
	 * 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmRecheckDtl> findBillOmRecheckDtlByShowBox(@Param("params") Map<String, Object> params)
			throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public SumUtilMap<String, Object> selectRfSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 打印
	 * 
	 * @param recheck
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNo(BillOmRecheck recheck) throws ServiceException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtl4Print(BillOmRecheckDtlSizeKind recheck) throws ServiceException;

	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNoAndBox(BillOmRecheck recheck) throws ServiceException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtlBox4Print(BillOmRecheckDtlSizeKind recheck)
			throws ServiceException;

	public List<String> selectAllDtlSizeKind(BillOmRecheck recheck) throws ServiceException;
	
	/**
	 * 更新分货复核明细
	 * @param recheck
	 * @param checkList
	 * @throws ServiceException
	 */
	public void updateOmRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> updateList,SystemUser user) throws ServiceException;
	
	/**
	 * 删除分货复核明细
	 * @param recheck
	 * @param updateList
	 * @throws ServiceException
	 */
	public void deleteOmRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> deleteList,SystemUser user) throws ServiceException;
	
	/**
	 * 更新拣货复核明细
	 * @param recheck
	 * @param insertList TODO
	 * @param checkList
	 * @throws ServiceException
	 */
	public void updateOmOutstockRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> updateList, List<BillOmRecheckDtl> insertList,SystemUser user) throws ServiceException;
	
	/**
	 * 删除拣货复核明细
	 * @param recheck
	 * @param updateList
	 * @throws ServiceException
	 */
	public void deleteOmOutstockRecheckDtl(BillOmRecheck recheck,List<BillOmRecheckDtl> deleteList,SystemUser user) throws ServiceException;
	
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
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 复核明细汇总查询总数箱号汇总
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findRecheckDtlGroupByBoxCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;
	
	public int insertRecheckDtl4UmCheck(Map<String, Object> params) throws ServiceException;
	
	public int insertRecheckDtl4SmOtherin(Map<String, Object> params) throws ServiceException;

}