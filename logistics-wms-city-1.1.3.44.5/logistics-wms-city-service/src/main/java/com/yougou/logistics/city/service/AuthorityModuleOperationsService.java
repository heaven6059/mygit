package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityModuleOperations;

public interface AuthorityModuleOperationsService extends BaseCrudService {
	/**
	 * 拥有操作权限的模块
	 * @return
	 */
	public List<AuthorityModuleOperations> findHasOperatorModules()throws ServiceException;
}