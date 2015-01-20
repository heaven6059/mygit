package com.yougou.logistics.city.common.enums;

/**
 * 退仓通知单状态
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillUmUntreadDtlStatusEnums {
	STATUS10("10", "新建"), STATUS13("13", "完成");
	private String status;
	private String desc;

	private BillUmUntreadDtlStatusEnums(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public String getStatus() {
		return this.status;
	}

	public String getDesc() {
		return this.desc;
	}
}
