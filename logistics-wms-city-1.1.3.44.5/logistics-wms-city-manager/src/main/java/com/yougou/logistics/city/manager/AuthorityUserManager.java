package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.uc.common.api.dto.AuthorityMenuDto;
import com.yougou.logistics.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.uc.common.api.dto.UserModuleResult;
import com.yougou.logistics.uc.common.api.model.AuthorityUser;

/**
 * 用户接口Manager
 * 
 * @author jiang.ys
 * @date 2014-2-11 上午10:19:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface AuthorityUserManager {

	/**
	 * 查询用户拥有的模块信息
	 * @param userId
	 * @param areaSystemId
	 * @return
	 */
	public UserModuleResult findUserHasModulesWithOperations(int userId, int areaSystemId, int curPage, int pageSize)
			throws ManagerException;

	/**
	 * 查询用户所拥有的全部模块信息
	 * @param userId
	 * @param areaSystemId
	 * @return
	 */
	public List<AuthorityUserModuleDto> findAllUserHasModules(int userId, int areaSystemId) throws ManagerException;

	/**
	 * 查询用户所拥有的全部菜单模块信息
	 * 模块已挂到菜单上,isLeaf=true为叶子节点即模块
	 * @param userId
	 * @param areaSystemId
	 * @return
	 */
	public AuthorityMenuDto findAllUserHasMenusAndModules(int userId, int areaSystemId) throws ManagerException;

	/**
	 * 查询机构下所有的用户
	 * @param storeNo
	 * @return
	 * @throws ManagerException
	 */
	public List<AuthorityUser> findUserListByStoreNo(String storeNo) throws ManagerException;
	/**
	 * 查询机构下所有的用户
	 * 指定区域系统
	 * @param storeNo
	 * @param systemId
	 * @param areaSystemId
	 * @return
	 * @throws ManagerException
	 */
	public List<AuthorityUser> findUserListByStoreNoAndSystemId(String storeNo,int systemId,int areaSystemId) throws ManagerException;

	/**
	 * 添加用户信息并关联角色
	 * @param u
	 * @param roleCode
	 * @return
	 * @throws ManagerException
	 */
	public boolean addUserWithRole(AuthorityUser u, int roleCode) throws ManagerException;

	/**
	 * 根据登陆名修改除用户登陆名、密码以外的用户信息
	 * @param u
	 * @return
	 * @throws ManagerException
	 */
	public boolean modifyUser(AuthorityUser u) throws ManagerException;

	/**
	 * 根据登录名查询用户
	 * @param loginName
	 * @return
	 * @throws ManagerException 
	 */
//	public List<AuthorityUser> findAuthorityUserByLoginName(String loginName) throws ManagerException;
}
