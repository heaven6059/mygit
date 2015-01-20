/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmUntreadMmDtlMapper
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmUntreadMmDtlMapper extends BaseCrudMapper {
	public List<BillUmUntreadMmDtl> selectItem(@Param("page") SimplePage page, @Param("params") BillUmUntreadMmDtl dtl)
			throws DaoException;

	public int selectItemCount(@Param("params") BillUmUntreadMmDtl dtl) throws DaoException;

	public Integer selectMaxRowId(@Param("params") BillUmUntreadMm dtl) throws DaoException;

	//删除所有明细
	public void deleteAllDetail(@Param("params") BillUmUntreadMm mm) throws DaoException;

	//更新所有明细的状态
	public void updateAllDetail(@Param("params") BillUmUntreadMm mm) throws DaoException;

	//汇总明细
	public List<BillUmUntreadMmDtl> selectDetail4Direct(@Param("params") BillUmUntreadMm mm) throws DaoException;

	public List<BillUmUntreadMmDtl> selectStoreNo(@Param("params") BillUmUntreadMm mm) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params) throws DaoException;
}