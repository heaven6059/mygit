package com.yougou.logistics.city.common.enums;

public enum BillConvertStatusEnums {
	
	STATUS_10("10","新建"),
	STATUS_11("11","已审核");
	
	private String value;
	private String desc;
	
	private BillConvertStatusEnums(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
