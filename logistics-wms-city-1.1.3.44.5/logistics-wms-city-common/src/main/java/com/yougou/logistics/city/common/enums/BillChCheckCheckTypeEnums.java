package com.yougou.logistics.city.common.enums;

/**
 * 盘点类别
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckCheckTypeEnums {
	CHECKTYPE1("1", "盘点"), CHECKTYPE2("2", "复盘");
	private String checkType;

	private BillChCheckCheckTypeEnums(String checkType, String text) {
		this.checkType = checkType;
	}

	public String getCheckType() {
		return this.checkType;
	}
}
