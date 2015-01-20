package com.yougou.logistics.city.common.enums;

/**
 * 盘点状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckDefferentFlagEnums {
	FLAG0("0", "无差异"), FLAG1("1", "有差异");
	private String flag;

	private BillChCheckDefferentFlagEnums(String flag, String text) {
		this.flag = flag;
	}

	public String getFlag() {
		return this.flag;
	}
}
