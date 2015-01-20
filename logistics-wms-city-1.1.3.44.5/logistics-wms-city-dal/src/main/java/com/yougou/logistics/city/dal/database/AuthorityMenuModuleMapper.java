package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityMenuModule;

public interface AuthorityMenuModuleMapper extends BaseCrudMapper {
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> selectUserHasModules(int userId);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<AuthorityMenuModule> selectUserHasMenusAndModules(int userId);
	/**
	 * 
	 * @param menuId
	 * @return
	 */
	public int deleteByMenuId(int menuId);
}