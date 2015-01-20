package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.service.AuthorityOrganizationService;
import com.yougou.logistics.uc.common.api.dto.AuthorityOrganizationDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityRoleWithUserListDto;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2014-1-24 下午1:36:22
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("authorityOrganizationManager")
public class AuthorityOrganizationManagerImpl implements AuthorityOrganizationManager {

	@Resource
	private AuthorityOrganizationService authorityOrganizationService;
	@Override
	public boolean addAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto) {
		return authorityOrganizationService.addAuthorityOrganization(authorityOrganizationDto);
	}

	@Override
	public boolean deleteAuthorityOrganization(String storeNo) {
		return authorityOrganizationService.deleteAuthorityOrganization(storeNo);
	}

	@Override
	public boolean updateAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto) {
		return authorityOrganizationService.updateAuthorityOrganization(authorityOrganizationDto);
	}

	@Override
	public List<AuthorityRoleWithUserListDto> findRoleListWithUserListByOrganization(String organizNo, int systemId,
			int areaSystemId) throws ManagerException {
		try {
			return authorityOrganizationService.findRoleListWithUserListByOrganization(organizNo, systemId, areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

}
