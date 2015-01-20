package com.yougou.logistics.city.common.model;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-30 下午5:38:52
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityUserinfo extends AuthorityUserinfoKey {

	private String loginName;

	private String username;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
