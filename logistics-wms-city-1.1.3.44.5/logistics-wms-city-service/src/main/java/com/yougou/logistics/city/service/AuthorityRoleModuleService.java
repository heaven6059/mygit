package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;

public interface AuthorityRoleModuleService extends BaseCrudService {
	 public  int deleteByPrimaryKey(AuthorityRoleModule record) throws ServiceException;
}