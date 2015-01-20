/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper
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
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmUntreadDtlMapper extends BaseCrudMapper {

	public void deleteAllDetail(@Param("params") BillUmUntread untread) throws DaoException;

	public void updateAllDetail(@Param("params") BillUmUntread untread) throws DaoException;

	public List<BillUmUntreadDtl> selectItemInBox(@Param("params") BillUmUntreadDtl dtl) throws DaoException;

	public void deleteItemByBox(@Param("params") BillUmUntreadDtl dtl) throws DaoException;

	public Integer selectMaxRowId(@Param("params") BillUmUntread untread) throws DaoException;

	public List<BillUmUntreadDtl> selectAllBox(@Param("params") BillUmUntread untread,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int selectCountByBox(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 查询明细表中的箱子
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmUntreadDtl> selectByPageByBox(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 根据主键及箱商品编码尺码更新数量
	 * 
	 * @param billUmUntreadDtl
	 * @return
	 * @throws DaoException
	 */
	public int updateByItemAndKey(BillUmUntreadDtl billUmUntreadDtl) throws DaoException;

	public List<ConBoxDtl> select4Box(@Param("page") SimplePage page, @Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int select4BoxCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public Set<String> selectRepeat(@Param("item") BillUmUntread untread) throws DaoException;

	public List<BillUmUntreadDtl> selectAllDetail(BillUmUntread untread) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 更新店退仓单的收货数量=计划数量
	 * @param untreadList
	 * @return
	 * @throws DaoException
	 */
	public int updateUntreadReceiptQtyByUntreadNo(@Param("params") Map<String, Object> params,@Param("list") List<BillUmUntread> untreadList) throws DaoException;
	
	/**
	 * 更新店退仓单的收货数量=0
	 * @param untreadList
	 * @return
	 * @throws DaoException
	 */
	public int updateUntreadReceiptQtyByCheckTaskNo(@Param("params") Map<String, Object> params,@Param("list") List<BillUmCheckTask> taskList) throws DaoException;
	
	/**
	 * 更新明细的收货数量为0
	 * @param untread
	 * @return
	 * @throws DaoException
	 */
	public int updateUntreadDtlReceiptQty(BillUmUntread untread) throws DaoException;
}