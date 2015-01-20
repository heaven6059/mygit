package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
public interface ConBoxDtlMapper extends BaseCrudMapper {

	public List<ConBoxDtl> findByParam(Map<String, Object> queryParam) throws DaoException;

	public int countItemAndSize(@Param("params") ConBoxDtl cc, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public List<ConBoxDtl> findCnBoxAndNumPage(@Param("page") SimplePage page, @Param("params") ConBoxDtl cc,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int selectItem4umuntreadCount(@Param("params") Map<String, String> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<ConBoxDtl> selectItem4umuntread(@Param("params") Map<String, String> map,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public ConBoxDtl selectBoxDtl4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
	
	public void deleteBoxDtl4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
	
	public Integer selectMaxBoxId(ConBoxDtl boxDtl) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
}