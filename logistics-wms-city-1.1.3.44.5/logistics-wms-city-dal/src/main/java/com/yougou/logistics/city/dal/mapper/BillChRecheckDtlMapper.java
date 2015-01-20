/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChRecheckDtlMapper
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtlDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillChRecheckDtlMapper extends BaseCrudMapper {

	/**
	 * 汇总明细
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectReCheckCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillChRecheckDtlDto> selectReCheck(@Param("params") Map<String, Object> map,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 查询盘点下的所有储位
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public List<BillChRecheckDtl> selectCellNo(@Param("check") BillChRecheckDtl check) throws DaoException;

	/***
	 * 查询最大的rowId
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public Integer selectMaxRowId(@Param("dtl") BillChRecheckDtl dtl) throws DaoException;

	/**
	 * 批量更新
	 * 
	 * @param list
	 * @throws DaoException
	 */
	public void batchUpdate(List<BillChRecheckDtl> list) throws DaoException;

	/**
	 * 批量插入
	 * 
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsert(List<BillChRecheckDtl> list) throws DaoException;

	public List<BillChRecheckDtl> selectRepeat(@Param("item") BillChRecheckDtl recheckDtl) throws DaoException;

	public BillChRecheckDtl selectFirstDtl(@Param("item") BillChRecheckDtl recheckDtl) throws DaoException;

	/**
	 * 按计划保存
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int saveByPlan(@Param("item") BillChRecheckDtl check) throws DaoException;

	/**
	 * 实盘置零
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int resetPlan(@Param("item") BillChRecheckDtl check) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 根据盘点计划单号，更新复盘点单状态
	 * @param billChCheck
	 * @return
	 * @throws DaoException
	 */
	public int updateStatusByPlanNo(@Param("params") Map<String, Object> map) throws DaoException;
	
	public SumUtilMap<String, Object> selectChReCheckSumQty(@Param("params") Map<String, Object> map, 
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

}