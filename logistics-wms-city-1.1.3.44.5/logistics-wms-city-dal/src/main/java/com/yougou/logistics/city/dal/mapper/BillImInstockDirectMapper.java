/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillImInstockDirectMapper
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
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
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillImInstockDirectMapper extends BaseCrudMapper {

	public BillImInstockDirect selectDetail(BillImInstockDirect direct) throws DaoException;

	public int countInstockDirectByMainId(@Param("params") BillImInstockDirect objEntiy,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImInstockDirect> findInstockDirectByMainIdPage(@Param("page") SimplePage page,
			@Param("params") BillImInstockDirect objEntiy, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty4CheckDirect(@Param("params") BillImInstockDirect map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public int countInstockDirectByType(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImInstockDirect> findInstockDirectByTypePage(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public SumUtilMap<String, Object> selectInstockDirectByTypePage4Sum(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);
}
