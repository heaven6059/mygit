package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;

public interface AuthorityRoleModuleManager extends BaseCrudManager {
	public  int deleteByPrimaryKey(AuthorityRoleModule record) throws ManagerException;
}