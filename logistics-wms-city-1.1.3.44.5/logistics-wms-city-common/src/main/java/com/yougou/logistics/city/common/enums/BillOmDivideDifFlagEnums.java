package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-11-22 上午9:57:16
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillOmDivideDifFlagEnums {
	
	//差异标示  0-正常数据;1-差异调整
	DIF_FLAG0("0"), DIF_FLAG1("1");

	private String optBillType;

	private BillOmDivideDifFlagEnums(String optBillType) {
		this.optBillType = optBillType;
	}

	public String getOptBillType() {
		return this.optBillType;
	}
}
