/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillConQualityChangeDtlMapper
 * @author luo.hl
 * @date  Thu Oct 24 13:54:46 CST 2013
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

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto2;

public interface BillConQualityChangeDtlMapper extends BaseCrudMapper {
	/**
	 * 查询详情数量
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public int selectDetailCount(@Param("dto") BillConQualityChangeDtlDto dto) throws DaoException;

	/**
	 * 查询详情
	 * @param dto
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillConQualityChangeDtlDto> selectDetail(@Param("page") SimplePage page,
			@Param("dto") BillConQualityChangeDtlDto dto) throws DaoException;

	/**
	 * 尺码横排查询商品详情
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public List<BillConQualityChangeDtlDto2> selectItemDetail(@Param("dto") BillConQualityChangeDtlDto dto)
			throws DaoException;
}