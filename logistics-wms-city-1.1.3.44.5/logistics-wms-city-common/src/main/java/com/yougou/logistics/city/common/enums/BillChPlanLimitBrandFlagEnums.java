package com.yougou.logistics.city.common.enums;

public enum BillChPlanLimitBrandFlagEnums {
	limitBrandFlag_0("0","抽盘"),
	limitBrandFlag_1("1","全盘");
	private String status;
	private String desc;

	private BillChPlanLimitBrandFlagEnums(String status, String desc) {
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
