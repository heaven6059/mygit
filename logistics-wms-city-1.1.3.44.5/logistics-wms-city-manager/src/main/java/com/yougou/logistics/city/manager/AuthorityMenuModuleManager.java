package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.AuthorityMenuModule;

public interface AuthorityMenuModuleManager extends BaseCrudManager {
	/**
	 * 
	 * @param modules
	 * @return
	 * @throws ManagerException
	 */
	public int save(int menuId,List<AuthorityMenuModule> modules) throws ManagerException;
}