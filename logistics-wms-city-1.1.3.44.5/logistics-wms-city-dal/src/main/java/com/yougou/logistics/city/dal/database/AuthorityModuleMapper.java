package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.AuthorityModuleDto;
import com.yougou.logistics.city.common.model.AuthorityModule;

public interface AuthorityModuleMapper extends BaseCrudMapper {
	/**
	 * 根据菜单编号查询模块
	 * @param menuId
	 * @return
	 */
	public List<AuthorityModule> selectAllByMenuId(int menuId);
	/**
	 * 查询所有菜单下的模块
	 * @return
	 */
	public List<AuthorityModuleDto> selectAllMenusWithModules(); 
	/**
	 * 
	 * @param menuId
	 * @return
	 */
	public int selectModuleCountByMenuId(int menuId);
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<AuthorityModuleDto> selectUserHasModules(int userId);
	/**
	 * 
	 * @param module
	 * @return
	 */
	public boolean selectModuleIsExistByName(AuthorityModule module);
	
}