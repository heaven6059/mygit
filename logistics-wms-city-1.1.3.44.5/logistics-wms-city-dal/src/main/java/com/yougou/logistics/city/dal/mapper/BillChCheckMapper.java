/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChCheckMapper
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillChCheckMapper extends BaseCrudMapper {
	public List<BillChCheckDto> selectChCheck(@Param("params") Map<String, Object> map, @Param("page") SimplePage page,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 根据盘点计划单号，更新盘点单状态
	 * @param billChCheck
	 * @return
	 * @throws DaoException
	 */
	public int updateStatusByPlanNo(BillChCheck billChCheck) throws DaoException;
	
	public SumUtilMap<String, Object> selectChCheckSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查找定位信息之外新增加的锁定储位编码
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectCellNo4Add(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 查询盘点计划单下面的盘点单
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectChCheckContainerFlag(@Param("params") Map<String, Object> map) throws DaoException;
	
	public SumUtilMap<String, Object> selectChCheckPlanSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	

}