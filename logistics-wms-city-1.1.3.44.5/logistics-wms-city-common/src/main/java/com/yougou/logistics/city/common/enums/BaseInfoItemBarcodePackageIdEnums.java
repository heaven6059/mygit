package com.yougou.logistics.city.common.enums;

public enum BaseInfoItemBarcodePackageIdEnums {
	
	MAIN("0","主条码"),
	DEPUTY("1","从条码");
	
	private String status;
	private String text;
	
	private BaseInfoItemBarcodePackageIdEnums(String status, String text) {
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
		BaseInfoItemBarcodePackageIdEnums[] statusArr = BaseInfoItemBarcodePackageIdEnums.values();
		for (BaseInfoItemBarcodePackageIdEnums barcodePackageIdEnums : statusArr) {
			if (barcodePackageIdEnums.getStatus().equals(status)) {
				return barcodePackageIdEnums.getText();
			}
		}
		return status;
	}
}
