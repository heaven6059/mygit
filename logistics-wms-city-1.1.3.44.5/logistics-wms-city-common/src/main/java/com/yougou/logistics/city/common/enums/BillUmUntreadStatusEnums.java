package com.yougou.logistics.city.common.enums;

/**
 * 退仓单状态
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillUmUntreadStatusEnums {
	STATUS10("10", "新建"), STATUS11("11", "已审核"),STATUS20("20", "差异收货"), STATUS25("25", "收货完成"), STATUS30("30", "差异验收"), STATUS35(
			"35", "验收完成"), STATUS99("99","作废");
	private String status;
	private String desc;

	private BillUmUntreadStatusEnums(String status, String desc) {
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
