package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.AuthorityModule;
import com.yougou.logistics.city.common.vo.AuthorityModuleVo;

/**
 * 
 * 模块管理
 * 
 * @author wei.b
 * @date 2013-8-22 下午2:34:11
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface AuthorityModuleService extends BaseCrudService {
	/**
	 * 
	 * 查询所有模块与菜单下的模块
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public AuthorityModuleVo findAllModulesAndUsedModules(int menuId)throws ServiceException;
	/**
	 * 
	 * @param menuId
	 * @return
	 * @throws ServiceException
	 */
	public int findModuleCountByMenuId(int menuId)throws ServiceException;
	
	/**
	 * 
	 */
	public Boolean findModuleIsExistByName(AuthorityModule module)throws ServiceException;
}