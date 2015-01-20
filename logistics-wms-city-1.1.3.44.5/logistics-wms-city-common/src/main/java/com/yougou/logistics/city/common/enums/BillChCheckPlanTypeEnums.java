package com.yougou.logistics.city.common.enums;

/**
 *盘点类型
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckPlanTypeEnums {
	PLANTYPE1("0", "商品盘"), PLANTYPE2("1", "储位盘");
	private String planType;

	private BillChCheckPlanTypeEnums(String planType, String text) {
		this.planType = planType;
	}

	public String getPlanType() {
		return this.planType;
	}
}
