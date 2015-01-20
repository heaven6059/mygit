package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.model.AuthorityMenu;
import com.yougou.logistics.city.service.AuthorityMenuService;
import com.yougou.logistics.city.service.AuthorityModuleService;

@Service("authorityMenuManager")
class AuthorityMenuManagerImpl extends BaseCrudManagerImpl implements AuthorityMenuManager {
    @Resource
    private AuthorityMenuService authorityMenuService;
    
    @Resource
    private AuthorityModuleService authorityModuleService;

    @Override
    public BaseCrudService init() {
        return authorityMenuService;
    }

	@Override
	public AuthorityMenuDto findAllAuthorityMenu(int menuId) throws ManagerException {
		try {
			return this.authorityMenuService.findAllAuthorityMenu(menuId);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public AuthorityMenu findAuthorityMenuWithIsLeaf(AuthorityMenu authorityMenu) throws ManagerException {
		try {
			return this.authorityMenuService.findAuthorityMenuWithIsLeaf(authorityMenu);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public boolean addAuthorityMenu(AuthorityMenu authorityMenu) throws ManagerException {
		try {
			int count=this.authorityModuleService.findModuleCountByMenuId(authorityMenu.getParentId());
			if(count>0){
				return false;
			}else{
				this.authorityMenuService.add(authorityMenu);
				return true;
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public AuthorityMenuDto findUserHasMenus(int userId,int menuId) throws ManagerException {
		try {
			return this.authorityMenuService.findUserHasMenus(userId,menuId);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public AuthorityMenuDto findHasAdminMenu(int menuId) throws ManagerException {
		try {
			return this.authorityMenuService.findHasAdminMenu(menuId);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
}