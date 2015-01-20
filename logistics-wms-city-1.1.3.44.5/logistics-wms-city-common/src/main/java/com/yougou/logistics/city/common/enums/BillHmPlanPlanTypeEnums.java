package com.yougou.logistics.city.common.enums;
/**
 * 退厂计划计划类型11-批发退货；12-批发召回；13-自营退货；14-自营召回
 * 
 * @author ye.kl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0
 * @copyright yougou.com
 */
public enum BillHmPlanPlanTypeEnums {
	PLANTYPE11("11", "批发退货"), PLANTYPE12("12", "批发退回"), PLANTYPE13("13", "自营退货"), PLANTYPE14("14", "自营召回");
    private String status;
    private String desc;

    private BillHmPlanPlanTypeEnums(String status, String desc) {
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
