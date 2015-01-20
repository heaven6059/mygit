package com.yougou.logistics.city.common.enums;


/**
 * 基础资料状态枚举类
 * 
 * @author xian.yq
 * @date 2013-9-3 下午6:56:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BaseInfoStatusEnums {
	
	ENABLED("0","有效"),
	DISABLED("1","无效");
	
	private String status;
	private String text;
	
	private BaseInfoStatusEnums(String status, String text) {
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
		BaseInfoStatusEnums[] statusArr = BaseInfoStatusEnums.values();
		for (BaseInfoStatusEnums baseInfoStatusEnums : statusArr) {
			if (baseInfoStatusEnums.getStatus().equals(status)) {
				return baseInfoStatusEnums.getText();
			}
		}
		return status;
	}
	
	
	
	

}
