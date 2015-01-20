package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityModuleOperations;

public interface AuthorityModuleOperationsMapper extends BaseCrudMapper {
	/**
	 * 有控制权限所有模块
	 * @return
	 */
	public List<AuthorityModuleOperations> selectHasOperatorModules();
}