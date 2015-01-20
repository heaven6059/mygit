package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CmDefstock;

/**
 * 
 * 通道service
 * 
 * @author qin.dy
 * @date 2013-9-26 下午4:02:36
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefstockService extends BaseCrudService {
	
	public int queryStoreNo0(CmDefstock cmDefstock) throws ServiceException;
	
	public int queryStoreNo1(CmDefstock cmDefstock) throws ServiceException;
	
	public int queryStoreNo2(CmDefstock cmDefstock) throws ServiceException;
}