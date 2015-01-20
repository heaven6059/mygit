package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.uc.common.api.dto.AuthorityMenuDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.uc.common.api.dto.UserModuleResult;
import com.yougou.logistics.uc.common.api.model.AuthorityUser;
import com.yougou.logistics.uc.common.api.service.AuthorityUserApi;

/**
 * 用户接口实现
 * 
 * @author jiang.ys
 * @date 2014-2-11 上午10:12:38
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("authorityUserService")
public class AuthorityUserServiceImpl implements AuthorityUserService {

	@Resource(name = "authorityUserApi")
	private AuthorityUserApi authorityUserApi;
	@Override
	public UserModuleResult findUserHasModulesWithOperations(int userId, int areaSystemId, int curPage, int pageSize)
			throws ServiceException {
		try {
			return authorityUserApi.findUserHasModulesWithOperations(userId, areaSystemId, curPage, pageSize);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public List<AuthorityUserModuleDto> findAllUserHasModules(int userId, int areaSystemId) throws ServiceException {
		try {
			return authorityUserApi.findAllUserHasModules(userId, areaSystemId);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public AuthorityMenuDto findAllUserHasMenusAndModules(int userId, int areaSystemId) throws ServiceException {
		try {
			return authorityUserApi.findAllUserHasMenusAndModules(userId, areaSystemId);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public List<AuthorityUser> findUserListByStoreNo(String storeNo) throws ServiceException {
		try {
			return authorityUserApi.findUserListByStoreNo(storeNo);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public List<AuthorityUser> findUserListByStoreNoAndSystemId(String storeNo, int systemId, int areaSystemId)
			throws ServiceException {
		try {
			return authorityUserApi.findUserListByStoreNoAndSystemId(storeNo, systemId, areaSystemId);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public boolean addUserWithRole(AuthorityUser u, int roleCode) throws ServiceException {
		try {
			return authorityUserApi.addUserWithRole(u, roleCode);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	public boolean modifyUser(AuthorityUser u) throws ServiceException {
		try {
			return authorityUserApi.modifyUser(u);
		} catch (RpcException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

//	@Override
//	public List<AuthorityUser> findAuthorityUserByLoginName(String loginName) throws ServiceException {
//		try {
//			return authorityUserApi.findAuthorityUserByLoginName(loginName);
//		} catch (RpcException e) {
//			throw new ServiceException(e.getMessage(),e);
//		}
//	}

}
