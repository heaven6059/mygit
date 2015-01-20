package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-2-22 下午7:33:07
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum DateConContentReportEnums {
	
	RUKU("0","入库"),
	CHUKU("1","出库"),
	KUNEI("2","库内");
	
	private String status;
	private String text;
	
	private DateConContentReportEnums(String status, String text) {
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
		DateConContentReportEnums[] statusArr = DateConContentReportEnums.values();
		for (DateConContentReportEnums conContentReportEnums : statusArr) {
			if (conContentReportEnums.getStatus().equals(status)) {
				return conContentReportEnums.getText();
			}
		}
		return status;
	}
}
