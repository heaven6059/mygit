package com.yougou.logistics.city.common.enums;

public enum BillOmOutstockOperateEnums {
	
	CUSTOMER ("0","客户拣货"),
	AREA("1","库区拣货");
	
	private String status;
	private String text;
	
	private BillOmOutstockOperateEnums(String status, String text) {
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
		BillOmOutstockOperateEnums[] statusArr = BillOmOutstockOperateEnums.values();
		for (BillOmOutstockOperateEnums billOmOutstockOperateEnums : statusArr) {
			if (billOmOutstockOperateEnums.getStatus().equals(status)) {
				return billOmOutstockOperateEnums.getText();
			}
		}
		return status;
	}
	
}
