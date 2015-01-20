package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-12-20 上午10:46:10
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum ResultMessageEnums {
	SUCCESS("success"), ERROR("error");

	private String message;

	private ResultMessageEnums(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
