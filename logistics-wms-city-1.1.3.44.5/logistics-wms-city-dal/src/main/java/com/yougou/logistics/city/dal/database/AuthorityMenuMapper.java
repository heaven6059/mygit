package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.model.AuthorityMenu;

public interface AuthorityMenuMapper extends BaseCrudMapper {
	/**
	 * 
	 * @param menuId
	 * @return
	 */
	public AuthorityMenuDto selectByPrimaryKey4Dto(int menuId);
	/**
	 * 
	 * @param menuParentId
	 * @return
	 */
	public List<AuthorityMenuDto> selectByParentId(int menuParentId);
	/**
	 * 
	 * @param authorityMenuDto
	 * @return
	 */
	public int selectCountByParentId(AuthorityMenu authorityMenu);
}