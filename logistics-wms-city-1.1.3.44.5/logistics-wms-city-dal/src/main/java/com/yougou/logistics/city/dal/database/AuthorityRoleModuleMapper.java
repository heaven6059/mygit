package com.yougou.logistics.city.dal.database;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;

public interface AuthorityRoleModuleMapper extends BaseCrudMapper {
	
	 public  int deleteByPrimaryKey(AuthorityRoleModule record);
}