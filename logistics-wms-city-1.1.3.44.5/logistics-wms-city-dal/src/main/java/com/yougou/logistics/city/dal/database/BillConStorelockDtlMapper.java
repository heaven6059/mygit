/*
 * 类名 com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillConStorelockDtlMapper extends BaseCrudMapper {
	
	/**
	 * 查询ROWID
	 * @param storelockDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectMaxPid(BillConStorelockDtl storelockDtl) throws DaoException;
	
	/**
	 * 新增明细
	 * @param storelock
	 */
	public void saveStorelockDtl(List<BillConStorelockDtl> list) throws DaoException;
	
	/**
	 * 根据储位删除明细
	 * @param storelockDtl
	 * @return
	 * @throws DaoException
	 */
	public int deleteModelByCellNo(List<BillConStorelockDtl> list) throws DaoException;
	
	/**
	 * 删除明细
	 * @param storelockDtl
	 * @return
	 * @throws DaoException
	 */
	public int deleteModel(BillConStorelockDtl storelockDtl) throws DaoException;
	
	/**
	 * 查询对应的储位数据
	 * @param storelockDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectStorelockDtlByCellNo(BillConStorelockDtl storelockDtl) throws DaoException;

	/**
	 * 查询客户库存锁定分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectConContentGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询客户库存锁定分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectConContentGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	
	/**
	 * 查询客户库存锁定数据
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectConContentGroup(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询客户库存锁定明细分页数
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectStorelockGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询客户库存锁定明细分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectStorelockGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	/**
	 * 根据储位查询库存
	 * @param storelockDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectConContentByCellNo(BillConStorelockDtl storelockDtl) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询退厂计划明细中的商品库存
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectWmPlanDtlInnerStock(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 查询是否存在预上预下的库存数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectPlanStockNum(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
     * 查询库存锁定需要转换的明细
     * @param billWmRequestDtl
     * @return
     * @throws DaoException
     */
	public List<BillConStorelockDtl> selectToWmRequestDtlGroupBy(BillConStorelockDtl billConStorelockDtl) throws DaoException;
	
	/**
	 * 更新库存锁定明细状态
	 * @param billConStorelockDtl
	 * @return
	 * @throws DaoException
	 */
	public int updateStorelockDtlStatus(BillConStorelockDtl billConStorelockDtl) throws DaoException;
	
	/**
	 * 查找可转退厂申请的锁定库存明细
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> select4WmPlan(@Param("params") Map<String, Object> params) throws DaoException;
	 
	/**
	 * 查询客户波茨在下架指示对应有多少个锁定数据
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectOutstockLeftStoreLock(@Param("params") Map<String, Object> params) throws DaoException;
	
	
	
	/**
	 * 查询客户库存锁定明细数据
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConStorelockDtl> selectStorelockGroup(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
}