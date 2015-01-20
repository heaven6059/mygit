package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.BillWmRecedeDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public interface BillWmRecedeDtlMapper extends BaseCrudMapper {
	
	public short selectMaxPid(BillWmRecedeDtlKey billWmRecedeDtlKey)throws DaoException;
	
	public int selectCountMx(@Param("dto")BillWmRecedeDtlDto dto,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoByExpNo(BillWmRecedeDtlDto dto)throws DaoException;

	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoGroupBy(@Param("page") SimplePage page,@Param("dto")BillWmRecedeDtlDto dto,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;

	public int selectItemCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
    
	public List<BillWmRecedeDtl> selectItem(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public int selectItemCountTest(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDtl> selectItemTest(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public void batchInsertDtl(List<BillWmRecedeDtl> list);  
	
	public int checkItem(@Param("params")Map<String,Object> params);
	
	
	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public int selectWmRecedeDtlDispatchCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	/**
	 * 查询退厂调度明细
	 * @param params
	 * @return
	 */
	public List<BillWmRecedeDispatchDtlDTO> selectWmRecedeDtlDispatchByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	/**
	 * 退厂调度明细汇总
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectDispatchSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
}