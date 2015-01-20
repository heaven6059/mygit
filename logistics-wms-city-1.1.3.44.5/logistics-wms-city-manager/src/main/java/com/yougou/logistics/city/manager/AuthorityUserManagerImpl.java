package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.service.AuthorityUserService;
import com.yougou.logistics.uc.common.api.dto.AuthorityMenuDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.uc.common.api.dto.UserModuleResult;
import com.yougou.logistics.uc.common.api.model.AuthorityUser;

/**
 * 用户接口Manager实现
 * 
 * @author jiang.ys
 * @date 2014-2-11 上午10:20:45
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("authorityUserManager")
public class AuthorityUserManagerImpl implements AuthorityUserManager {

	@Resource
	private AuthorityUserService authorityUserService;
	
	@Override
	public UserModuleResult findUserHasModulesWithOperations(int userId, int areaSystemId, int curPage, int pageSize)
			throws ManagerException {
		try {
			return authorityUserService.findUserHasModulesWithOperations(userId, areaSystemId, curPage, pageSize);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<AuthorityUserModuleDto> findAllUserHasModules(int userId, int areaSystemId) throws ManagerException {
		try {
			return authorityUserService.findAllUserHasModules(userId, areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public AuthorityMenuDto findAllUserHasMenusAndModules(int userId, int areaSystemId) throws ManagerException {
		try {
			return authorityUserService.findAllUserHasMenusAndModules(userId, areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<AuthorityUser> findUserListByStoreNo(String storeNo) throws ManagerException {
		try {
			return authorityUserService.findUserListByStoreNo(storeNo);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<AuthorityUser> findUserListByStoreNoAndSystemId(String storeNo, int systemId, int areaSystemId)
			throws ManagerException {
		try {
			return authorityUserService.findUserListByStoreNoAndSystemId(storeNo, systemId, areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public boolean addUserWithRole(AuthorityUser u, int roleCode) throws ManagerException {
		try {
			return authorityUserService.addUserWithRole(u, roleCode);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public boolean modifyUser(AuthorityUser u) throws ManagerException {
		try {
			return authorityUserService.modifyUser(u);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

//	@Override
//	public List<AuthorityUser> findAuthorityUserByLoginName(String loginName) throws ManagerException {
//		try {
//			return authorityUserService.findAuthorityUserByLoginName(loginName);
//		} catch (ServiceException e) {
//			throw new ManagerException(e.getMessage());
//		}
//	}

}
