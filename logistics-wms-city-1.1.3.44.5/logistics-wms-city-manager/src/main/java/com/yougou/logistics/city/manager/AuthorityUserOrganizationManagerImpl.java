package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.AuthorityUserOrganizationDto;
import com.yougou.logistics.city.service.AuthorityUserOrganizationService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Feb 10 14:51:59 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("userOrganizationManager")
class AuthorityUserOrganizationManagerImpl extends BaseCrudManagerImpl implements AuthorityUserOrganizationManager {
    @Resource
    private AuthorityUserOrganizationService authorityUserOrganizationService;

    @Override
    public BaseCrudService init() {
        return authorityUserOrganizationService;
    }

	@Override
	public List<AuthorityUserOrganizationDto> findUserOrganizationByParams(Map<String, Object> params)
			throws ManagerException {
		try{
			return authorityUserOrganizationService.findUserOrganizationByParams(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
    
    
}
