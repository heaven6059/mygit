package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityModuleOperations;
import com.yougou.logistics.city.dal.database.AuthorityModuleOperationsMapper;

@Service("authorityModuleOperationsService")
class AuthorityModuleOperationsServiceImpl extends BaseCrudServiceImpl implements AuthorityModuleOperationsService {
    @Resource
    private AuthorityModuleOperationsMapper authorityModuleOperationsMapper;

    @Override
    public BaseCrudMapper init() {
        return authorityModuleOperationsMapper;
    }

	@Override
	public List<AuthorityModuleOperations> findHasOperatorModules()throws ServiceException{
		try {
			return this.authorityModuleOperationsMapper.selectHasOperatorModules();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
    
}