/*
 * 类名 com.yougou.logistics.city.dal.mapper.ItemBarcodeMapper
 * @author qin.dy
 * @date  Sat Nov 09 15:00:22 CST 2013
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
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface ItemCellContentMapper extends BaseCrudMapper {
	public int selectCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams,
			@Param("list") List<String> list);

	public List<?> selectByPage(@Param("page") SimplePage page,
			@Param("orderByField")String orderByField, @Param("orderBy")String orderBy,
			@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams,
			@Param("list") List<String> list);
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams,@Param("list")String[] areaNos)throws DaoException;
}