package com.yougou.logistics.city.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityResources;
import com.yougou.logistics.city.service.AuthorityResourcesService;

@Service("authorityResourcesManager")
public class AuthorityResourcesManagerImpl implements AuthorityResourcesManager {
	
	@Autowired
	private AuthorityResourcesService authorityResourcesService;

	@Override
	public AuthorityResourcesDTO queryAllAuthorityResources(Long menuId) throws ManagerException {
		try {
			return this.authorityResourcesService.queryAllAuthorityResources(menuId);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public AuthorityResourcesDTO queryResourceById(Long menuId) throws ManagerException {
		try {
			return this.authorityResourcesService.queryResourceById(menuId);
		} catch (Exception e) {
			throw new ManagerException();
		}
	}

	@Override
	public AuthorityResourcesDTO addResource(AuthorityResources authorityResources) throws ManagerException {
		this.authorityResourcesService.addResource(authorityResources);
		AuthorityResourcesDTO dto;
		try {
			dto = this.authorityResourcesService.queryResourceById(authorityResources.getMenuId());
			return dto;
		}  catch (Exception e) {
			throw new ManagerException();
		}
	}
	
	@Override
	public AuthorityResourcesDTO updateResource(AuthorityResources authorityResources) throws ManagerException {
	//	AuthorityResources ar=new AuthorityResources();
//		ar.setMenuId(authorityResourcesVO.getMenuId());
//		ar.setMenuName(authorityResourcesVO.getMenuName());
//		ar.setMenuUrl(authorityResourcesVO.getMenuurl());
//		ar.setType(authorityResourcesVO.getType());
//		ar.setSort(authorityResourcesVO.getSort());
//		ar.setRemark(authorityResourcesVO.getRemark());
		this.authorityResourcesService.updateResource(authorityResources);
		try {
			return this.authorityResourcesService.queryResourceById(authorityResources.getMenuId());
		}  catch (Exception e) {
			throw new ManagerException();
		}
	}

	public AuthorityResourcesService getAuthorityResourcesService() {
		return authorityResourcesService;
	}

	public void setAuthorityResourcesService(
			AuthorityResourcesService authorityResourcesService) {
		this.authorityResourcesService = authorityResourcesService;
	}

	@Override
	public int removeResourceById(Long menuId) {
		return this.authorityResourcesService.removeResourceById(menuId);
	}
	
}