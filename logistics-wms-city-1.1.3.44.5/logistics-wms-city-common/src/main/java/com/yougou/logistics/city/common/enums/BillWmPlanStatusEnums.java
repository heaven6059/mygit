package com.yougou.logistics.city.common.enums;

/**
 * 退厂计划状态
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0
 * @copyright yougou.com
 */
public enum BillWmPlanStatusEnums {
    STATUS10("10", "新建"), STATUS11("11", "已审核"), STATUS20("20", "已转移库"), STATUS30(
	    "30", "已转锁定"), STATUS40("40", "已转申请");
    private String status;
    private String desc;

    private BillWmPlanStatusEnums(String status, String desc) {
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
