package com.yougou.logistics.city.common.enums;


/**
 * 盘点类型
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChPlanTypeEnums {
	ITEM("0","商品盘"),
	CELL("1","储位盘");
	private String value;
	private String text;

	private BillChPlanTypeEnums(String value, String text) {
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
		BillChPlanTypeEnums[] statusArr = BillChPlanTypeEnums.values();
		for (BillChPlanTypeEnums status : statusArr) {
			if (status.getValue().equals(value)) {
				return status.getText();
			}
		}
		return value;
	}
}
