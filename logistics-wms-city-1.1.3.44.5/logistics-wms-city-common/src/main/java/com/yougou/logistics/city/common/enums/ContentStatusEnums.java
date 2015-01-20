package com.yougou.logistics.city.common.enums;

public enum ContentStatusEnums {
	STATUS_0("0","可用"),
	STATUS_1("1","锁定");

	private String status;
	private String desc;

	private ContentStatusEnums(String status, String desc) {
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
