package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-1-4 上午11:44:08
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum ResultEnums {
	SUCCESS("success"), FAIL("fail");
	private String resultMsg;

	private ResultEnums(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getResultMsg() {
		return this.resultMsg;
	}
}
