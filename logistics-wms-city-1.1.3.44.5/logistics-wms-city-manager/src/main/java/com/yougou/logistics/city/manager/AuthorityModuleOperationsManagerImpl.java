package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.AuthorityModuleOperationsService;

@Service("authorityModuleOperationsManager")
class AuthorityModuleOperationsManagerImpl extends BaseCrudManagerImpl implements AuthorityModuleOperationsManager {
    @Resource
    private AuthorityModuleOperationsService authorityModuleOperationsService;

    @Override
    public BaseCrudService init() {
        return authorityModuleOperationsService;
    }
}