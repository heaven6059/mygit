package com.yougou.logistics.city.common.enums;


/**
 * 盘点定位状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckDirectStautsEnums {
	CREATE("10","新建"),COMPLETE("13","完成"),CANCEL("16","取消");
	private String value;
	private String text;

	private BillChCheckDirectStautsEnums(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	public static String getText(String value){
		BillChCheckDirectStautsEnums[] statusArr = BillChCheckDirectStautsEnums.values();
		for (BillChCheckDirectStautsEnums status : statusArr) {
			if (status.getValue().equals(value)) {
				return status.getText();
			}
		}
		return value;
	}
}
