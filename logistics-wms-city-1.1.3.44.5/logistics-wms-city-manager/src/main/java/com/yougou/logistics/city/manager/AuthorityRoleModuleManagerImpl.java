package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;
import com.yougou.logistics.city.service.AuthorityRoleModuleService;

@Service("authorityRoleModuleManager")
class AuthorityRoleModuleManagerImpl extends BaseCrudManagerImpl implements AuthorityRoleModuleManager {
    @Resource
    private AuthorityRoleModuleService authorityRoleModuleService;

    @Override
    public BaseCrudService init() {
        return authorityRoleModuleService;
    }
    public  int deleteByPrimaryKey(AuthorityRoleModule record) throws ManagerException{
    	try {
			return authorityRoleModuleService.deleteByPrimaryKey(record);
		} catch (Exception e) {
			throw new ManagerException();
		}
    }
}