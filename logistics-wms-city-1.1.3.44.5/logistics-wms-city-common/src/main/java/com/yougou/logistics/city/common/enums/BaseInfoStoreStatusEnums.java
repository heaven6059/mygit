package com.yougou.logistics.city.common.enums;

/**
 * 基础资料机构状态枚举类
 * 
 * @author su.yq
 * @date 2013-9-4 下午12:10:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BaseInfoStoreStatusEnums {
	
	CLOSE("1", "关闭"), NORMAL("0", "正常"), CHECK("2", "盘点");

	private BaseInfoStoreStatusEnums(String status, String text) {
		this.status = status;
		this.text = text;
	}

	private String status;
	private String text;

	public String getStatus() {
		return status;
	}

	public String getText() {
		return text;
	}

	public static String getTextByStatus(String status) {
		BaseInfoStoreStatusEnums[] statusArr = BaseInfoStoreStatusEnums.values();
		for (BaseInfoStoreStatusEnums baseInfoStoreStatusEnums : statusArr) {
			if (baseInfoStoreStatusEnums.getStatus().equals(status)) {
				return baseInfoStoreStatusEnums.getText();
			}
		}
		return "";
	}
}
