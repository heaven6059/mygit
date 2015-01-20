package com.yougou.logistics.city.common.enums;

public enum BillStatusTransEnums {
	
	NOTTRANSMISSION("0","未传输"),
	HASTRANSMISSION("1","已传输");
	
	private String status;
	private String text;
	
	private BillStatusTransEnums(String status, String text) {
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
		BillStatusTransEnums[] statusArr = BillStatusTransEnums.values();
		for (BillStatusTransEnums billStatusTransEnums : statusArr) {
			if (billStatusTransEnums.getStatus().equals(status)) {
				return billStatusTransEnums.getText();
			}
		}
		return status;
	}
}
