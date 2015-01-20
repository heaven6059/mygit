package com.yougou.logistics.city.common.enums;

/**
 * 盘点状态
 * 
 * @author ye.kl
 * @date 2014-4-8 下午4:35:00
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckDtlStautsEnums {
	STATUS10("10", "建单"), STATUS13("13", "结案");
	private String status;
	private String text;
	
	private BillChCheckDtlStautsEnums(String status, String text) {
		this.status = status;
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public String getText() {
		return text;
	}
}
