package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;
import com.yougou.logistics.city.dal.database.AuthorityRoleModuleMapper;

@Service("authorityRoleModuleService")
class AuthorityRoleModuleServiceImpl extends BaseCrudServiceImpl implements AuthorityRoleModuleService {
    @Resource
    private AuthorityRoleModuleMapper authorityRoleModuleMapper;

    @Override
    public BaseCrudMapper init() {
        return authorityRoleModuleMapper;
    }
    
    public  int deleteByPrimaryKey(AuthorityRoleModule record) throws ServiceException{
    	try {
			return authorityRoleModuleMapper.deleteByPrimaryKey(record);
		} catch (Exception e) {
			throw new ServiceException();
		}
    }
}