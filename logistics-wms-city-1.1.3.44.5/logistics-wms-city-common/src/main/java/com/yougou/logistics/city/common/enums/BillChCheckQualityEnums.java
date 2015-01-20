package com.yougou.logistics.city.common.enums;

/**
 * 盘点状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckQualityEnums {
	QUALITY0("0", "良品"), QUALITY1("A", "不良品");
	private String quality;

	private BillChCheckQualityEnums(String quality, String text) {
		this.quality = quality;
	}

	public String getQuality() {
		return this.quality;
	}
}
