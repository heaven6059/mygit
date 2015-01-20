package com.yougou.logistics.city.common.enums;

public enum CmDefcellCheckStatusEnums {
	CHECKSTATUS_0("0","可用"),
	CHECKSTATUS_3("3","盘点");
	private String status;
	private String desc;

	private CmDefcellCheckStatusEnums(String status, String desc) {
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
