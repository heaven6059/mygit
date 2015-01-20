package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.AuthorityMenuModuleMapper;

@Service("authorityMenuModuleService")
class AuthorityMenuModuleServiceImpl extends BaseCrudServiceImpl implements AuthorityMenuModuleService {
    @Resource
    private AuthorityMenuModuleMapper authorityMenuModuleMapper;

    @Override
    public BaseCrudMapper init() {
        return authorityMenuModuleMapper;
    }

	@Override
	public int deleteByMenuId(int menuId) throws ServiceException {
		try {
			return this.authorityMenuModuleMapper.deleteByMenuId(menuId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
    
}