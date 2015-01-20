package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.AuthorityOperationsService;

@Service("authorityOperationsManager")
class AuthorityOperationsManagerImpl extends BaseCrudManagerImpl implements AuthorityOperationsManager {
    @Resource
    private AuthorityOperationsService authorityOperationsService;

    @Override
    public BaseCrudService init() {
        return authorityOperationsService;
    }
}