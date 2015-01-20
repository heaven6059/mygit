/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChPlanDtlMapper
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillChPlanDtl;

public interface BillChPlanDtlMapper extends BaseCrudMapper {
	public Long selectMaxId();
	
	public int deleteByPlanNo(BillChPlanDtl billChPlanDtl);
	
	public int deleteByBillKey(BillChPlanDtl billChPlanDtl);
	/**
	 * 批量插入盘点计划单明细
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertDtl(List<BillChPlanDtl> list) throws DaoException;
	
	public List<BillChPlanDtl> selectDuplicateRecord(@Param("params") Map<String, Object> params) throws DaoException;
	public List<Map<String,Object>> selectCellNo(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 批量插入全盘储位
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public Integer batchInsertPlanDtl4AllCell(@Param("params") Map<String, Object> params) throws DaoException;
}