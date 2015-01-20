package com.yougou.logistics.city.common.enums;

/**
 * 退仓上架状态
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillUmInstockStatusEnums {
	STATUS10("10", "新建"), STATUS13("13", "已审核");
	private String status;
	private String desc;

	private BillUmInstockStatusEnums(String status, String desc) {
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
