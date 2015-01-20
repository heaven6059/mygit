/*
 * 类名 com.yougou.logistics.city.dal.database.BillUmCheckMapper
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
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmCheckMapper extends BaseCrudMapper {

	/**
	 * 退仓验收单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillUmCheck> selectBillUmCheckByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 退仓验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectBillUmCheckCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 审核退仓收货单存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procBillUmCheckAuditQuery(Map<String, String> map) throws DaoException;

	public int selectCountUmNoForInstock(@Param("params") Map map) throws DaoException;

	public List<BillUmCheck> selectByPageUmNoForInstock(@Param("page") SimplePage page, @Param("params") Map map)
			throws DaoException;

	public int selectCountCheckNoForInstock(@Param("params") Map map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillUmCheck> selectByPageCheckNoForInstock(@Param("page") SimplePage page, @Param("params") Map map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int selectCount4loadBox(@Param("params") Map map, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public List<BillUmCheck> select4loadBoxByPage(@Param("params") Map map, @Param("page") SimplePage page,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public int selectCountUmNo(@Param("params") Map map) throws DaoException;
	
	public SumUtilMap<String, Object> selectUntreadJoinCheckDtlSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public SumUtilMap<String, Object> selectUntreadJoinCheckSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 验证是否存在不是已审核的单据
	 * @param checkList
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmCheck> selectCheckValidate(@Param("params")BillUmCheck check,@Param("list") List<BillUmCheck> checkList) throws DaoException;
	
	 public void updateCheckStatus4Convert(@Param("params") Map<String, Object> params,@Param("list")List<BillUmCheck> list) throws DaoException;
	 
	 public void updateRollbackCheckStatus4Convert(@Param("params") Map<String, Object> params,@Param("list")List<BillUmCheck> list) throws DaoException;
}