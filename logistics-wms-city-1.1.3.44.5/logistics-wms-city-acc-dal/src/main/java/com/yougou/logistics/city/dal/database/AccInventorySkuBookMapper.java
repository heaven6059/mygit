package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccInventorySkuBook;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-11 15:24:23
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
public interface AccInventorySkuBookMapper extends BaseCrudMapper {
	
	/**
	 * 查询最近一条skubook(锁定更新)
	 * @param list
	 * @throws DaoException
	 */
	public AccInventorySkuBook selectLatestOneforUpdateByParams(@Param("params")Map<String,Object> params) throws DaoException;
	
	/**
	 * 批量插入明细
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertSkuBook(List<AccInventorySkuBook> list) throws DaoException;
	
}