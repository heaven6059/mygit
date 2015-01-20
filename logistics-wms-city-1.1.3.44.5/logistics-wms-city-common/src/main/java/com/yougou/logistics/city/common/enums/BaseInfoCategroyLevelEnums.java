package com.yougou.logistics.city.common.enums;

/**
 * 基础资料商品分类类别级别枚举类
 * 
 * @author su.yq
 * @date 2013-9-4 下午12:10:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BaseInfoCategroyLevelEnums {
	
	LARGE("1", "大"), MEDIUM("2", "中"), SMALL("3", "小");

	private BaseInfoCategroyLevelEnums(String status, String text) {
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

	public static String getTextByLevels(String status) {
		BaseInfoCategroyLevelEnums[] statusArr = BaseInfoCategroyLevelEnums.values();
		for (BaseInfoCategroyLevelEnums baseInfoCategroyLevelEnums : statusArr) {
			if (baseInfoCategroyLevelEnums.getStatus().equals(status)) {
				return baseInfoCategroyLevelEnums.getText();
			}
		}
		return "";
	}
}
