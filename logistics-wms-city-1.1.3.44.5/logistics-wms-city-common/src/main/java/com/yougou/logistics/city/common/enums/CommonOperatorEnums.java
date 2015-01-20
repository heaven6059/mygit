package com.yougou.logistics.city.common.enums;
/**
 * @author wei.b
 * @email wei.b@yougou.com
 * create time: 2013-6-26
 */
public enum CommonOperatorEnums {
	DELETED("deleted"),
	UPDATED("updated"),
	INSERTED("inserted"),
	MAININFO("maininfo"); // 主表信息
	
	private String operator;
	
	CommonOperatorEnums(String operator){
		this.operator=operator;
	}

	public String getOperator() {
		return operator;
	}
}
