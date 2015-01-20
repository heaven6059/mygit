package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.uc.common.api.dto.AuthorityOrganizationDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityRoleWithUserListDto;
import com.yougou.logistics.uc.common.api.service.AuthorityOrganizationApi;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2014-1-24 上午11:23:06
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("authorityOrganizationService")
public class AuthorityOrganizationServiceImpl implements AuthorityOrganizationService {

	@Resource(name = "authorityOrganizationApi")
	private AuthorityOrganizationApi authorityOrganizationApi;
	@Override
	public boolean addAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto) {
		return authorityOrganizationApi.addAuthorityOrganization(authorityOrganizationDto);
	}

	@Override
	public boolean deleteAuthorityOrganization(String storeNo) {
		return authorityOrganizationApi.deleteAuthorityOrganization(storeNo);
	}

	@Override
	public boolean updateAuthorityOrganization(AuthorityOrganizationDto authorityOrganizationDto) {
		return authorityOrganizationApi.updateAuthorityOrganization(authorityOrganizationDto);
	}

	@Override
	public List<AuthorityRoleWithUserListDto> findRoleListWithUserListByOrganization(String organizNo, int systemId,
			int areaSystemId) throws ServiceException {
		try {
			return authorityOrganizationApi.findRoleListWithUserListByOrganization(organizNo, systemId, areaSystemId);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

}
