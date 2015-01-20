package com.yougou.logistics.city.common.enums;
/**
 * 储位-混载标示
 * @author jiang.ys
 *
 */
public enum CmDefCellMixFlagEnums {
	MIXFLAG_0("0","不可混"),
	MIXFLAG_1("1","同商品不同属性混"),
	MIXFLAG_2("2","不同商品混");
	private String mixFlag;
	private String desc;

	private CmDefCellMixFlagEnums(String mixFlag, String desc) {
		this.mixFlag = mixFlag;
		this.desc = desc;
	}

	public String getMixFlag() {
		return this.mixFlag;
	}

	public String getDesc() {
		return this.desc;
	}
}
