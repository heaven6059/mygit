/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillSmOtherinDtlMapper
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.BillSmOtherinPrintDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;
public interface BillSmOtherinDtlMapper extends BaseCrudMapper {
	
	public int selectContentCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmOtherinDtlDto> selectContent(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	public SumUtilMap<String, Object> selectPageSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	public List<BillSmOtherinDtl> selectContentParams(@Param("model")BillSmOtherinDtl modelType,@Param("params")Map<String,Object> params);
	public List<BillSmOtherinDtl> selectContentDtl(@Param("model")BillSmOtherinDtl modelType,@Param("params")Map<String,Object> params);
	public int updateContent(BillSmOtherinDtl modelType)throws DaoException;
	
	public int selectMaxPid(BillSmOtherinDtl modelType)throws DaoException;
	public int selectIsHave(BillSmOtherinDtl modelType)throws DaoException;
	
	public List<BillSmOtherinDtl> selectDuplicateRecord(@Param("params") Map<String, Object> params) throws DaoException;
	/**
	 * 批量插入明细
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertDtl(List<BillSmOtherinDtl> list) throws DaoException;
	/**
	 * 尺码横排
	 * @param modelType
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillSmOtherinDtl> selectDtlSysNo(@Param("model")BillSmOtherinDtl modelType, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	public int selectSysNoContentCount(@Param("model")BillSmOtherinDtl params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmOtherinDtl> selectSysNoContentByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("model")BillSmOtherinDtl params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmOtherinDtl> selectSysNoByPage(@Param("model")BillSmOtherinDtl cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	public int selectOtherinDtlRecheckQtySum(@Param("params")Map<String,Object> params)throws DaoException;
	public void updateRecheckQty4Convert(@Param("params") Map<String,Object> params)throws DaoException;
	/**
	 * 查询打印尺码横排所需要的所有明细
	 * @param params
	 * @return
	 */
	public List<BillSmOtherinPrintDto> selectPrintDtl4Size(@Param("params") Map<String,Object> params);
	public void updateOperateRecord(Map<String, Object> map) throws DaoException;
}