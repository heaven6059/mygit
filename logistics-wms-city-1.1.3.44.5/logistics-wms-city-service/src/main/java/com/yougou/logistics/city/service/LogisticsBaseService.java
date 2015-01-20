package com.yougou.logistics.city.service;


import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.exception.SqlSessionServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.LogisticsBase;

/**
 * 
 * @author wei.b
 *
 */
public interface LogisticsBaseService extends BaseCrudService {

	public abstract void findLogisticsBase(String key) throws SqlSessionServiceException;

	public abstract void addLogisticsBase(String key, LogisticsBase logisticsBase) throws ServiceException;

	public abstract int findCount() throws ServiceException;

	public abstract void addLogisticBaseByConnection(String key, LogisticsBase logisticsBase) throws ServiceException;

}
