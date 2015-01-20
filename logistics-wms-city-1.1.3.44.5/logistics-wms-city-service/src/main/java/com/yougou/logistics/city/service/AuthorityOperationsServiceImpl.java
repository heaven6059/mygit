package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.AuthorityOperationsMapper;

@Service("authorityOperationsService")
class AuthorityOperationsServiceImpl extends BaseCrudServiceImpl implements AuthorityOperationsService {
    @Resource
    private AuthorityOperationsMapper authorityOperationsMapper;

    @Override
    public BaseCrudMapper init() {
        return authorityOperationsMapper;
    }
}