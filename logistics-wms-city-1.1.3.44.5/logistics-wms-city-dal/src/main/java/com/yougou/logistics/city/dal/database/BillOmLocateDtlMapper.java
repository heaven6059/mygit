/*
 * 类名 com.yougou.logistics.city.dal.database.BillOmLocateDtlMapper
 * @author su.yq
 * @date  Mon Nov 04 14:35:57 CST 2013
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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.BillOmLocateDtlSizeKind;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillOmLocateDtlMapper extends BaseCrudMapper {

	public List<BillOmLocateDtl> selectBillOmLocateDtlGroupBy(@Param("params") Map<String, Object> params);

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 分组查询明细
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNoItemNoExpNo(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillOmLocateDtlSizeKind> selectAllDtl4Print(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<String> selectAllDtlSizeKind(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNo(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<BillOmLocateDtl> selectLocateExpNoGroupBy(@Param("params") Map<String, Object> map) throws DaoException;
	
	public int deleteLocateDtlByLocateNo(BillOmLocate billOmLocate) throws DaoException;
	
	/**
	 * 根据发货通知单查询
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmLocateDtl> selectLocateDtlByExpNo(@Param("params") Map<String, Object> map) throws DaoException;
	
}