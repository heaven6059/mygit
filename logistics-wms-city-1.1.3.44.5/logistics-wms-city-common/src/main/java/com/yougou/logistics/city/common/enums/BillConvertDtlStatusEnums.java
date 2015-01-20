package com.yougou.logistics.city.common.enums;

public enum BillConvertDtlStatusEnums {
	
	STATUS_10("10","新建");
	
	private String value;
	private String desc;
	
	private BillConvertDtlStatusEnums(String value, String desc) {
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
