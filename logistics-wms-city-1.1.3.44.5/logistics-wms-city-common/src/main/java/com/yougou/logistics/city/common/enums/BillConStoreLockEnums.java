package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-3-21 下午4:30:10
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillConStoreLockEnums {
	
	BUSINESS_TYPE0("0", "正常锁定"), 
	BUSINESS_TYPE1("1", "退厂计划"), 
	BUSINESS_TYPE2("2", "移库计划"), 
	STORELOCK_TYPE0("0", "客户锁定"), 
	STORELOCK_TYPE1("1", "库存属性锁定"),
	STATUS10("10", "建单"),
	STATUS12("12", "已转退厂申请"),
	STATUS11("11", "审核"),
	STATUS90("90", "关单"),
	STATUS91("91", "手工关闭");

	
	private String status;
	private String desc;

	private BillConStoreLockEnums(String status, String desc) {
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
