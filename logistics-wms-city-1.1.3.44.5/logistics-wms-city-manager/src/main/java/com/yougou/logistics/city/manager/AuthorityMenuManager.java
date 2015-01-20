package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.model.AuthorityMenu;

public interface AuthorityMenuManager extends BaseCrudManager {
	/**
	 * 
	 * {@link #findHasAdminMenu(int)}
	 */
	@Deprecated
	public AuthorityMenuDto findAllAuthorityMenu(int menuId) throws ManagerException;
	/**
	 * 
	 * @param authorityMenu
	 * @return
	 * @throws ManagerException
	 */
	public AuthorityMenu findAuthorityMenuWithIsLeaf(AuthorityMenu authorityMenu) throws ManagerException;
	/**
	 * 
	 * @param authorityMenu
	 * @return
	 * @throws ManagerException
	 */
	public boolean addAuthorityMenu(AuthorityMenu authorityMenu) throws ManagerException;
	/**
	 * 
	 * @param userId
	 * @param menuId
	 * @return
	 * @throws ManagerException
	 */
	public AuthorityMenuDto findUserHasMenus(int userId,int menuId)throws ManagerException;
	/**
	 * 查询带admin权限的菜单
	 * @param menuId
	 * @return
	 * @throws ManagerException
	 */
	public AuthorityMenuDto findHasAdminMenu(int menuId)throws ManagerException;
}