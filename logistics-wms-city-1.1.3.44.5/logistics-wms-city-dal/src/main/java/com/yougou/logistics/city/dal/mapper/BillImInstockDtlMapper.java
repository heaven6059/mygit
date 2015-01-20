/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillImInstockDtlMapper
 * @author luo.hl
 * @date  Fri Oct 25 10:51:08 CST 2013
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
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto2;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillImInstockDtlMapper extends BaseCrudMapper {
	/**
	 * 根据商品编号分组查询数量
	 * 
	 * @param dtl
	 * @return
	 */
	public int selectItemCountBroupByItemNo(@Param("dtl") BillImInstockDtl dtl) throws DaoException;

	/**
	 * 根据商品编码分组查询
	 * 
	 * @param page
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillImInstockDtl> selectItemGroupByItemNo(@Param("page") SimplePage page,
			@Param("dtl") BillImInstockDtl dtl) throws DaoException;

	/**
	 * 根据单号，商品编号查询
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillImInstockDtlDto2> selectDetailByParams(@Param("dtl") BillImInstockDtl dtl) throws DaoException;

	/**
	 * 查询品牌库
	 * 
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public String selectSysNo(BillImInstockDtlDto dto) throws DaoException;

	/**
	 * 查询最大的ID
	 * 
	 * @param dto
	 * @return
	 */
	public int selectMaxInsotckId(@Param("dtl") BillImInstockDtl dto) throws DaoException;

	/**
	 * 查询同一商品的实际上架数总和
	 * 
	 * @param dto
	 * @return
	 */
	public int selectLeftQty(@Param("dtl") BillImInstockDtl dto) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public int saveByPlan(BillImInstock instock) throws DaoException;

	/**
	 * 查询实际上架储位是否有重复的
	 * 
	 * @param dtl
	 * @throws DaoException
	 */
	public List<BillImInstockDtl> selectRepeatRealCell(BillImInstockDtl dtl) throws DaoException;

	public BillImInstockDtlDto selectDtoByPrimaryKey(BillImInstockDtlDto dto);

	public List<BillImInstockDtlDto> selectPrintInf(BillImInstock instock);
	
	public int updateByAudit(BillImInstockDtl dtl) throws DaoException;
}