package com.yougou.logistics.city.common.enums;


/**
 * 盘点计划状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChPlanStatusEnums {
	CREATE("10","建单"),
	AUDIT("11","审核"),
	START("15","发起"),
	SEND("20","发单"),
	INITANDRECHECK("25","初盘/复盘"),
	CLOSE("90","关闭"),
	INVALID("99","作废");
	private String value;
	private String text;

	private BillChPlanStatusEnums(String value, String text) {
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
		BillChPlanStatusEnums[] statusArr = BillChPlanStatusEnums.values();
		for (BillChPlanStatusEnums status : statusArr) {
			if (status.getValue().equals(value)) {
				return status.getText();
			}
		}
		return value;
	}
}
