package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-3-18 下午3:09:12
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum StorelockTypeEnums {
	
	ENABLED("0","客户锁定"),
	DISABLED("1","库存属性锁定");
	
	private String status;
	private String text;
	
	private StorelockTypeEnums(String status, String text) {
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
		StorelockTypeEnums[] statusArr = StorelockTypeEnums.values();
		for (StorelockTypeEnums storelockTypeEnums : statusArr) {
			if (storelockTypeEnums.getStatus().equals(status)) {
				return storelockTypeEnums.getText();
			}
		}
		return status;
	}
	
}
