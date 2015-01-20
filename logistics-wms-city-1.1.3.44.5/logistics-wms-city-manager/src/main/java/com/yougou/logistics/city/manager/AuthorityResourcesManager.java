package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityResources;

public interface AuthorityResourcesManager {
	
	public AuthorityResourcesDTO queryAllAuthorityResources(Long menuId) throws ManagerException;
	
	public AuthorityResourcesDTO queryResourceById(Long menuId) throws ManagerException;
	
	public AuthorityResourcesDTO addResource(AuthorityResources authorityResources) throws ManagerException;
	      
	public AuthorityResourcesDTO updateResource(AuthorityResources authorityResources) throws ManagerException;
	
	public int removeResourceById(Long menuId);
}
