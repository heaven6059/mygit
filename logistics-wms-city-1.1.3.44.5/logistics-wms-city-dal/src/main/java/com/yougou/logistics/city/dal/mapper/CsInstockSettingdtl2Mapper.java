/*
 * 类名 com.yougou.logistics.city.dal.mapper.CsInstockSettingdtl2Mapper
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

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface CsInstockSettingdtl2Mapper extends BaseCrudMapper {
	/**
	 * 根据策略编码删除所有的储位
	 * @param obj
	 * @return
	 */
	public abstract int deleteBySettingNo(Object obj) throws DaoException;

	/**
	 * 查询库区
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectAreaBySettingNo(Map<String, String> paramMap) throws DaoException;

	/**
	 * 查询通道
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectStockBySettingNo(Map<String, String> paramMap) throws DaoException;

	/**
	 * 查询储位
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectCellBySettingNo(Map<String, String> paramMap) throws DaoException;
}