package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface AccInventorySkuService extends BaseCrudService {
	
	/**
	 * SKU库存记账
	 * map支持的参数：
	 * checkNo 验收单号
	 * cellId 储位
	 * @return
	 * @throws ServiceException
	 */
	public void addSkuConContentStore(Map<String,Object> params) throws ServiceException;
	
	/**
	 * 更新sku库存
	 * @param accInventorySku
	 * @return
	 * @throws ServiceException
	 */
	public int updateAccInventorySku(AccInventorySku accInventorySku) throws ServiceException;
	
	/**
	 * 根据conNo及cellNo查询sku
	 * @param params
	 */
	public List<AccInventorySku> selectByConNoCellNoParams(Map<String,Object> params) throws ServiceException;
	
}