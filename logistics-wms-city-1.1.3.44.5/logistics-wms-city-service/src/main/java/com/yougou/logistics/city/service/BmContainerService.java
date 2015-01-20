package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmContainer;
/**
 * 容器基本信息 
 * @author wanghb
 * @date   2014-7-30
 * @version 1.1.3.36
 */
public interface BmContainerService extends BaseCrudService {
	
	int batchUpdate(List<BmContainer> bmContainer)throws ServiceException;
	
	boolean checkBmContainerStatus(BmContainer bmContainer)throws ServiceException;
	/**
	 * 释放库存占用状态-库存调整
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	int batchStatusByBillConAdj(Map<String,Object> map)throws ServiceException;
	/**
	 * 更新con_box明细
	 */
	int batchConBoxStatusByBillConAdj(Map<String,Object> map)throws ServiceException;
}