package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.model.AuthorityMenu;

public interface AuthorityMenuService extends BaseCrudService {
	/**
	 * 
	 */
	public AuthorityMenuDto findAllAuthorityMenu(int menuId)throws ServiceException;
	
	/**
	 * 查询判断是否是叶子节点的菜单
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public AuthorityMenu findAuthorityMenuWithIsLeaf(AuthorityMenu authorityMenu)throws ServiceException;
	/**
	 * 计算子菜单的数量
	 * @param authorityMenu
	 * @return
	 * @throws ServiceException
	 */
	public int findCountByParentId(AuthorityMenu authorityMenu)throws ServiceException;
	
	/**
	 * 查询用户所拥有的菜单与模块
	 * @param userId
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public AuthorityMenuDto findUserHasMenus(int userId,int menuId)throws ServiceException;
	/**
	 * 查询有admin权限的菜单
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public AuthorityMenuDto findHasAdminMenu(int menuId) throws ServiceException;
}