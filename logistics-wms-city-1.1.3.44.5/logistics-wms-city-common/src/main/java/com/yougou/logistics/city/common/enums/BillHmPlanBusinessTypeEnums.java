package com.yougou.logistics.city.common.enums;
/**
 * 移库计划业务类型
 * 
 * @author ye.kl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0
 * @copyright yougou.com
 */
public enum BillHmPlanBusinessTypeEnums {
	BUSINESSTYPE0("0", "正常移库"), BUSINESSTYPE1("1", "退厂计划");
	    private String status;
	    private String desc;

	    private BillHmPlanBusinessTypeEnums(String status, String desc) {
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
