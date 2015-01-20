package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccInventorySku;

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
public interface AccInventorySkuMapper extends BaseCrudMapper {
	
	/**
	 * 加锁更新
	 * @param params
	 * @return 
	 */
	public int updateLockAccInventorySku(@Param("params")Map<String,Object> params);
	
	/**
	 * 更新数量、预上、预下
	 * @param AccInventorySku
	 */
	public void updateAccInventorySkuStockQtyByPrimaryKey(AccInventorySku accInventorySku);
	
	
	/**
	 * 删除数量、预上、预下都为0的记录
	 * @param AccInventorySku
	 */
	public int deleteAllStockIs0(AccInventorySku accInventorySku);
	
	/**
	 * 根据conNo及cellNo查询sku
	 * @param params
	 */
	public List<AccInventorySku> selectByConNoCellNoParams(@Param("params")Map<String,Object> params);
}