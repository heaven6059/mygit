package com.yougou.logistics.city.common.enums;

/**
 * 基础资料禁用启用状态枚举类
 * 
 * @author su.yq
 * @date 2013-9-4 下午12:10:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BaseInfoDisableAndEnabledEnums {

	ENABLED("0", "禁用"), DISABLED("1", "启用");

	private String status;
	private String text;

	private BaseInfoDisableAndEnabledEnums(String status, String text) {
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
		BaseInfoDisableAndEnabledEnums[] statusArr = BaseInfoDisableAndEnabledEnums.values();
		for (BaseInfoDisableAndEnabledEnums baseInfoDisableAndEnabledEnums : statusArr) {
			if (baseInfoDisableAndEnabledEnums.getStatus().equals(status)) {
				return baseInfoDisableAndEnabledEnums.getText();
			}
		}
		return status;
	}
}
