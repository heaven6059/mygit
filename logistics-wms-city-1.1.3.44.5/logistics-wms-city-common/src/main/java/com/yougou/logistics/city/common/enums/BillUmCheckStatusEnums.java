package com.yougou.logistics.city.common.enums;

/**
 * 验收单状态
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillUmCheckStatusEnums {
	STATUS2("2", "门店转货"),STATUS10("10", "新建"), STATUS11("11", "验收完成"),STATUS13("13", "差异验收"), STATUS20("20", "已装箱"), STATUS25("25", "已分配"),STATUS30("30", "已转货");
	private String status;
	private String desc;

	private BillUmCheckStatusEnums(String status, String desc) {
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
