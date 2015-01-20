package com.yougou.logistics.city.common.enums;

/**
 * 盘点状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckDirectStatusEnums {
	STATUS10("10", "新建"), 
	STATUS13("13", "完成"),
	CREATE("10","新建"),
	COMPLETE("13","完成"),
	CANCEL("16","取消");;
	private String status;
	private String text;
	private BillChCheckDirectStatusEnums(String status, String text) {
		this.status = status;
		this.text = text;
	}

	public String getStatus() {
		return this.status;
	}
	public String getText() {
		return this.text;
	}
	public static String getText(String value){
		BillChCheckDirectStatusEnums[] statusArr = BillChCheckDirectStatusEnums.values();
		for (BillChCheckDirectStatusEnums status : statusArr) {
			if (status.getStatus().equals(value)) {
				return status.getText();
			}
		}
		return value;
	}
}
