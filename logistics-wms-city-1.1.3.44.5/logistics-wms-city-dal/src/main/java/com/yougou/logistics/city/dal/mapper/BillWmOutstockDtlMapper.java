/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillWmOutstockDtlMapper
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillWmOutstockDtlMapper extends BaseCrudMapper {
	
	/**
	 * 选择退厂通知单带出明细
	 * @param authorityParams TODO
	 * @param billWmRecedeDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDtlDto> selectOutstockDtlItem(@Param("params") BillWmOutstockDtl billWmOutstockDtl, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 选择退厂通知单带出明细分页
	 * @param authorityParams TODO
	 * @param billWmRecedeDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmOutstockDtlDto> selectOutstockDtlItemByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	public int selectOutstockDtlItemCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;	
	
	
	public SumUtilMap<String, Object> selectOutstockDtlItemSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 更新拣货单明细
	 * @param billWmOutstockDtl
	 * @return
	 * @throws DaoException
	 */
	public int updateBillWmOutstockDtl(BillWmOutstockDtl billWmOutstockDtl) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
}