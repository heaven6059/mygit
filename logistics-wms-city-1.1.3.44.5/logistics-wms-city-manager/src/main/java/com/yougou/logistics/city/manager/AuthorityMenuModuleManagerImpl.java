package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityMenuModule;
import com.yougou.logistics.city.service.AuthorityMenuModuleService;

@Service("authorityMenuModuleManager")
class AuthorityMenuModuleManagerImpl extends BaseCrudManagerImpl implements AuthorityMenuModuleManager {
    @Resource
    private AuthorityMenuModuleService authorityMenuModuleService;
    
    @Override
    public BaseCrudService init() {
        return authorityMenuModuleService;
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int save(int menuId, List<AuthorityMenuModule> modules) throws ManagerException {
		try {
			int count=this.authorityMenuModuleService.deleteByMenuId(menuId);
			if(null!=modules&&modules.size()>0){
				for (AuthorityMenuModule authorityMenuModule : modules) {
					count+=this.authorityMenuModuleService.add(authorityMenuModule);
				}
			}
			return count;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
    
    
}