package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

public interface AuthorityMenuModuleService extends BaseCrudService {
	/**
	 * 
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public int deleteByMenuId(int menuId)throws ServiceException;
	
}