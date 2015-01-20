package com.yougou.logistics.city.common.enums;


/**
 * 盘点计划是否按照整个品牌盘点
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChPlanIsByBrandEnums {
	NOT("0","不限制"),
	IS("1","限制");
	private String value;
	private String text;

	private BillChPlanIsByBrandEnums(String value, String text) {
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
		BillChPlanIsByBrandEnums[] statusArr = BillChPlanIsByBrandEnums.values();
		for (BillChPlanIsByBrandEnums status : statusArr) {
			if (status.getValue().equals(value)) {
				return status.getText();
			}
		}
		return value;
	}
}
