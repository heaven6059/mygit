/*
 * 类名 com.yougou.logistics.city.dal.database.BillUmCheckTaskDtlMapper
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskDtl;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmCheckTaskDtlMapper extends BaseCrudMapper {

	/**
	 * 新增退仓验收任务明细
	 * @param untreadList
	 * @throws DaoException
	 */
	public int insertUmCheckTaskDtl(@Param("params") BillUmCheckTask umCheckTask,
			@Param("list") List<BillUmUntread> untreadList) throws DaoException;

	/**
	 * 删除退仓验收任务明细
	 * @param umCheckTask
	 * @return
	 * @throws DaoException
	 */
	public int deleteUmCheckTaskDtlByCheckTaskNo(BillUmCheckTask umCheckTask) throws DaoException;

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询退仓验收明细的所有店退仓单
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmCheckTaskDtl> selectUntreadNo4CheckTaskDtl(@Param("params") Map<String, Object> params)
			throws DaoException;
	
	/**
	 * 查询商品
	 * @param item
	 * @param page
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<Item> selectItem4CheckTask(@Param("params") Map<String, Object> params, @Param("page") SimplePage page,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询商品分页数量
	 * @param item
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectItemCount4CheckTask(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询最大的行号
	 * @param billHmPlanDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillUmCheckTask umCheckTask) throws DaoException;
	
	/**
	 * 查询添加的明细是否重复
	 * @param checkTask
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmCheckTaskDtl> selectDuplicateCheckTask(BillUmCheckTask checkTask) throws DaoException;
	
	/**
	 * 按计划保存
	 * @param params
	 * @throws DaoException
	 */
	public void saveCheckQty4itemQty(@Param("params") Map<String, Object> params) throws DaoException;
	
	/**
	 * 按单删除
	 * @param checkTaskDtl
	 * @return
	 * @throws DaoException
	 */
	public int deleteUmCheckTaskDtlByUntreadNo(BillUmCheckTaskDtl checkTaskDtl) throws DaoException;
}