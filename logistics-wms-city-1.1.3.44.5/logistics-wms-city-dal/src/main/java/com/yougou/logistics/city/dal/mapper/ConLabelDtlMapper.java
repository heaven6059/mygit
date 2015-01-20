/*
 * 类名 com.yougou.logistics.city.dal.mapper.ConLabelDtlMapper
 * @author qin.dy
 * @date  Mon Sep 30 15:10:42 CST 2013
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
import com.yougou.logistics.city.common.dto.CnLabelQueryDto;
import com.yougou.logistics.city.common.model.ConLabelDtl;

public interface ConLabelDtlMapper extends BaseCrudMapper {
	
	public  List<ConLabelDtl>  selectItemInfoByLabelNo(@Param("params") CnLabelQueryDto cnLabelQueryDto)throws DaoException;
	
	/**
	 * 修改标签表商品信息
	 * @param conLabel
	 * @return
	 * @throws DaoException
	 */
//	public int  modifyConLabelDtlByItemInfo(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 查询最大的ID
	 * @param conLabelDtl
	 * @return
	 */
	public int selectConLabelDtlMaxPid(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 查询标签明细根据商品信息
	 * @param conLabelDtl
	 * @return
	 */
//	public ConLabelDtl selectConLabelDtlByItemInfo(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 查询标签明细根据商品信息
	 * @param conLabelDtl
	 * @return
	 */
	public ConLabelDtl selectConLabelDtlByItemNoAndSize(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 更新标签明细商品数量
	 * @param conLabelDtl
	 * @return
	 */
	public int updateConLabelDtlQtyByItemInfo(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 更新标签明细状态
	 * @param conLabelDtl
	 * @return
	 * @throws DaoException
	 */
	public int modifyLabelDtlStatusByLabelNo(ConLabelDtl conLabelDtl)throws DaoException;
	
	/**
	 * 查询标签明细根据商品信息
	 * @param conLabelDtl
	 * @return
	 */
	public ConLabelDtl selectConLabelDtl4Recheck(@Param("params") Map<String, Object> params)throws DaoException;
	
	public void deleteConLabelDtl4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
	
}