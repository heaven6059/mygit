package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-4-1 上午9:42:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum AuthorityUserBrandEnums {
	STATUS0("0", "无效"), STATUS1("1", "有效");
	private String status;
	private String desc;

	private AuthorityUserBrandEnums(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public String getStatus() {
		return this.status;
	}

	public String getDesc() {
		return this.desc;
	}
}