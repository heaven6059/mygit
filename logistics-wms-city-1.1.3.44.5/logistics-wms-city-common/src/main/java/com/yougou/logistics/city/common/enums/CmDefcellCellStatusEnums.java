package com.yougou.logistics.city.common.enums;

public enum CmDefcellCellStatusEnums {
	CELLSTATUS_0("0","可用"),
	CELLSTATUS_1("1","禁用"),
	CELLSTATUS_2("2","冻结");

	private String status;
	private String desc;

	private CmDefcellCellStatusEnums(String status, String desc) {
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
