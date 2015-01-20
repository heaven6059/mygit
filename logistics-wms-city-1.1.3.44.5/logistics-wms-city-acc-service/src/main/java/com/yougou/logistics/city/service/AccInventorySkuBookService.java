package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AccInventorySku;
import com.yougou.logistics.city.common.model.AccTask;
import com.yougou.logistics.city.common.model.AccTaskDtl;
import com.yougou.logistics.city.common.vo.AccCheckDtlVo;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;

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
public interface AccInventorySkuBookService extends BaseCrudService {
	
	
	/**
	 * SkuBook库存记账
	 *  @param skuBookVo
	 * @return
	 * @throws ServiceException
	 */
	public void accountingForSku(AccInventorySkuBookVo skuBookVo) throws ServiceException;
	
	/**
	 * SkuBook库存记账(事务同步)
	 *  @param skuBookVo
	 * @return
	 * @throws ServiceException
	 */
	public void addSkuBookTran(AccInventorySkuBookVo skuBookVo) throws ServiceException;
	
	/**
	 * 按单据记sku及容器库存账(事务同步)
	 * @param params
	 * @return void
	 * @throws ServiceException
	 */
	public void accountForSkuConByBillTran(AccTask accTask) throws ServiceException;
	
	/**
	 * 按明细DetailRowId记sku及容器库存账(事务同步)
	 * @param params
	 * @return void
	 * @throws ServiceException
	 */
	public void accountForSkuConByDetailRowIdTran(AccTask accTask) throws ServiceException;
	
	
	/**
	 * 批量SkuBook库存记账
	 * @param accTask
	 *  @param list
	 * @return
	 * @throws ServiceException
	 */
	public void accountForSkuConList(AccTask accTask,List<AccCheckDtlVo> list)	throws ServiceException;
	
	
	/**
	 * 非任务方式按明细Detail记sku及容器库存账(事务同步)
	 * @param accTask
	 * @param accTaskDtl
	 * @return void
	 * @throws ServiceException
	 */
	public void accountForSkuConByDetailTranNoTask(AccTask accTask,AccTaskDtl accTaskDtl) throws ServiceException;
	
	/**
	 * 非任务方式按单据记sku及容器库存账(事务同步)
	 * @param params
	 * @return void
	 * @throws ServiceException
	 */
	public void accountForSkuConByBillTranNoTask(AccTask accTask) throws ServiceException;
	
	/**
	 * 删除数量、预上、预下都为0的记录
	 * @param params
	 * @return void
	 * @throws ServiceException
	 */
	public int deleteAllStockIs0(AccInventorySku accInventorySku) throws ServiceException;
	
	/**
	 * 更新储位的sku库存
	 * @param params
	 * billNo 单据号;
	 * billType 单据类型;
	 * ioFlag 进出标识(I=入库 O=出库);
	 * cellNo 储位编码;
	 * cellNo_d 目标储位编码
	 * conNo 容器编码
	 * @throws ServiceException
	 */
	public void updateCellNoConNoSkuStock(Map<String, Object> params) throws ServiceException;
	
	/**
	 * 按单据查询的准备参数
	 * (处理CA类型的cellNo)
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> SelectPrepareAccDtlParams(HashMap<String, Object> params)  throws ServiceException;
	
}