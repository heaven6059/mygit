package com.yougou.logistics.city.common.api.enums;

/**
 * 单据类型枚举
 * @author jiang.ys
 *
 */
public enum BillTypeEnum {

	TYPE_01("01","厂商入库单-指令"),
	TYPE_02("02","店退仓-指令"),
	TYPE_03("03","多款自动配货(店)-指令"),
	TYPE_04("04","仓出店单-差异"),
	TYPE_05("05","移仓申请单-指令"),
	TYPE_06("06","调货通知单-指令"),
	TYPE_07("07","多款自动配货(仓)-指令"),
	TYPE_08("08","移仓入-指令"),
	TYPE_09("09","移仓单-差异");
	private String value;
	private String chName;
	
	private BillTypeEnum(String value,String chName){
		this.value = value;
		this.chName = chName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}
	
	
}
