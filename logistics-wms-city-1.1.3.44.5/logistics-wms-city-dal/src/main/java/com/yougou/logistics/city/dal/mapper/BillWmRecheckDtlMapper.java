package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

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
public interface BillWmRecheckDtlMapper extends BaseCrudMapper {

	//	public Long  getItemIdByRecheckNo(BillWmRecheckDtl billWmRecheckDtl)throws DaoException;

	/**
	 * 查询最大ID
	 * @param billOmRecheckDtl
	 * @return
	 */
	public int selectMaxPid(BillWmRecheckDtl billWmRecheckDtl) throws DaoException;

	/**
	 * 查询退厂复核单数量
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectWmRecheckDtlCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询退厂复核单分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillWmRecheckDtl> selectWmRecheckDtlByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
    public List<BillWmRecheckDtl> findBillRecheckBox(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询退厂复核单箱明细汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectWmRecheckDtlGroupByCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询退厂复核单箱明细汇总分页
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillWmRecheckDtl> selectWmRecheckDtlGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	
	public SumUtilMap<String, Object> selectWmRecheckDtlSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}