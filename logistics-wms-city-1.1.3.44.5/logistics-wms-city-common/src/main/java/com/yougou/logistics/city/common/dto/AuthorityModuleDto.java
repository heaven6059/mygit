package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.AuthorityModule;

/**
 * 关联菜单的模块
 * 
 * @author wei.b
 * @date 2013-8-23 下午2:33:25
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityModuleDto extends AuthorityModule {

	private Integer menuId;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
}
