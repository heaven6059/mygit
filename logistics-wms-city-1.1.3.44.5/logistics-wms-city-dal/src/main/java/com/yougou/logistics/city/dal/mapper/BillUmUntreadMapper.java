/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
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
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;

public interface BillUmUntreadMapper extends BaseCrudMapper {

	public BillUmUntread selectDetail4Print(BillUmUntread untread)
			throws DaoException;

	/**
	 * 退仓任务查询店退仓单
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillUmUntread> selectUntread2CheckTask(
			@Param("page") SimplePage page,
			@Param("orderByField") String orderByField,
			@Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params,
			@Param("list") List<BillUmUntread> list,
			@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 保存退仓验收任务，更新来源店退仓单状态为15-已分配任务
	 * 
	 * @param list
	 * @return
	 * @throws DaoException
	 */
	public int updateUntreadStatus4CheckTask(
			@Param("params") Map<String, Object> params,
			@Param("list") List<BillUmUntread> list) throws DaoException;

	/**
	 * 批量更新退仓单状态
	 * 
	 * @param params
	 *            key:status
	 * @param list
	 * @return
	 * @throws DaoException
	 * @author wanghb
	 */
	public int batchUpdateUntreadStatus(
			@Param("params") Map<String, Object> params,
			@Param("list") List<BillUmUntread> list) throws DaoException;

	/**
	 * 更新店退仓单状态
	 * 
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int updateUntread2Status11(BillUmUntread untread)
			throws DaoException;

	/**
	 * 退仓单合计
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public Map<String, Object> selectSumQty(
			@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	/**
	 * 检查店退仓单是否是建单状态或者是否被删除
	 * @param billUmUntreadKey
	 * @param status
	 * @return
	 */
	public int judgeObjIsExist(@Param("params")BillUmUntreadKey billUmUntreadKey,@Param("param") String status);

}