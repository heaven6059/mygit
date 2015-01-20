package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.AuthorityUserOrganization;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-5-5 上午10:06:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityUserOrganizationDto extends AuthorityUserOrganization{
	private String storeName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}
