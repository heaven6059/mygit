package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.api.dto.AccInventoryConDto;
import com.yougou.logistics.city.common.api.dto.AccTaskDto;

/**
 * TODO: 增加描述
 * 
 * @author wu.gy
 * @date 2014-5-14 下午2:44:30
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface AccInventoryConSkuServiceApi {
	
	/**
	 * 绑板、解板、拼板、(不更新sku的装箱)业务调用
     * 记录容器总账及三级账库存变更
     * @param accInventoryConDto
     * 必填的字段CON_NO、LOCNO、CELL_NO、MOVE_CHILDREN_QTY、direction、CREATOR、accConInfoList
     * （accConInfoList不为空且CELL_NO不相同时会同步更新sku库存）
     */
	public String accontingForCon(AccInventoryConDto accInventoryConDto)throws ServiceException;
	
	/**
	 * 按单据记sku及容器库存账(事务同步)
	 * 记录SKU总账、SKU三级账、容器总账及三级账库存变更
	 * @param accTaskDto 必填字段： billNo、billType、ioFlag
	 * @return void
	 * @throws ServiceException
	 */
	public String accountForSkuConByBillTran(AccTaskDto accTaskDto) throws ServiceException;
	

}
