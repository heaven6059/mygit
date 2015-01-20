/*
 * 类名 com.yougou.logistics.city.dal.mapper.CsInstockSettingdtlMapper
 * @author luo.hl
 * @date  Tue Oct 08 09:58:17 CST 2013
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

public interface CsInstockSettingdtlMapper extends BaseCrudMapper {
	/**
	 * 根据策略编码删除所有的商品
	 * @param obj
	 * @return
	 */
	public abstract int deleteBySettingNo(Object obj) throws DaoException;

	/**
	 * 查询品牌
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectBrandBySettingNo(Map<String, String> paramMap) throws DaoException;

	/**
	 * 查询类别
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectCategoryBySettingNo(Map<String, String> paramMap)
			throws DaoException;

	/**
	 * 查询商品
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectItemBySettingNo(Map<String, String> paramMap) throws DaoException;

	/***
	 * 查询同一生效对象在整个策略中是否存在
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectSelectValueCount(@Param("param") Map<String, Object> map) throws DaoException;
}