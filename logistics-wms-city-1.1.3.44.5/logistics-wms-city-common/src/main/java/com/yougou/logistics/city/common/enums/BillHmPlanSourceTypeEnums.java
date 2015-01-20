package com.yougou.logistics.city.common.enums;
/**
 * 移库计划来源单号：11-批发退货；12-批发退回；13-自营退货；14-自营召回；
 * 
 * @author ye.kl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0
 * @copyright yougou.com
 */
public enum BillHmPlanSourceTypeEnums {
	SOURCETYPE11("11", "批发退货"), SOURCETYPE12("12", "批发退回"), SOURCETYPE13("13", "自营退货"), SOURCETYPE14("14", "自营召回");
    private String status;
    private String desc;

    private BillHmPlanSourceTypeEnums(String status, String desc) {
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
