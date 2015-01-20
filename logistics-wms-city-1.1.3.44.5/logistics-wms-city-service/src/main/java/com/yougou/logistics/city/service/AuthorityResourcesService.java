package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityResources;

public interface AuthorityResourcesService {
	
	public AuthorityResourcesDTO queryAllAuthorityResources(Long menuId) throws ServiceException;
	
	public AuthorityResourcesDTO queryResourceById(Long menuId) throws ServiceException;
	
	public int addResource(AuthorityResources authorityResources);
	
	public int updateResource(AuthorityResources authorityResources);
	
	public int removeResourceById(Long menuId);
	
	public AuthorityResourcesDTO queryAllAuthorityResourcesRefRoleId(Long menuId,Long roleId) throws ServiceException;
	
	
}

