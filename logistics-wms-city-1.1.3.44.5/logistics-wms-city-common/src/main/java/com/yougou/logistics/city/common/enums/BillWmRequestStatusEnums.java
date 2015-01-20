package com.yougou.logistics.city.common.enums;
/**
 * 退厂申请状态
 * 
 * @author ye.kl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0
 * @copyright yougou.com
 */
public enum BillWmRequestStatusEnums {
	STATUS10("10", "新建"), STATUS11("11", "已审核"), STATUS90("90", "关闭");
	    private String status;
	    private String desc;

	    private BillWmRequestStatusEnums(String status, String desc) {
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
