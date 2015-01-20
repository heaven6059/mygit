package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

public interface BmDeflocService extends BaseCrudService {
	
	/**
	 * 删除仓别
	 * @param locnoStrs
	 * @return
	 * @throws ManagerException
	 */
	public int deleteFefloc(String locnoStrs) throws ServiceException;
}