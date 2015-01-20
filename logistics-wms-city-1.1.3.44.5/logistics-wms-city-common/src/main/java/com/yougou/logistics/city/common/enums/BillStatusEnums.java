package com.yougou.logistics.city.common.enums;

public enum BillStatusEnums {
	
	MAKEBILL ("0","制单"),
	OVER("100","完结");
	
	private String status;
	private String text;
	
	private BillStatusEnums(String status, String text) {
		this.status = status;
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public String getText() {
		return text;
	}
	
	public static String getTextByStatus(String status) {
		BillStatusEnums[] statusArr = BillStatusEnums.values();
		for (BillStatusEnums billStatusEnums : statusArr) {
			if (billStatusEnums.getStatus().equals(status)) {
				return billStatusEnums.getText();
			}
		}
		return status;
	}
}
