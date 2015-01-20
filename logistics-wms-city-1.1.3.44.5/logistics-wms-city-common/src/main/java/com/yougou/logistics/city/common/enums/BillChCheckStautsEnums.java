package com.yougou.logistics.city.common.enums;

/**
 * 盘点状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckStautsEnums {
	STATUS10("10", "建单"), STATUS20("20", "已发单"), STATUS12("12", "完成"), STATUS25("25", "初盘/复盘回单"), STATUS90("90", "关闭"), STATUS13(
			"13", "结案");
	private String status;

	private BillChCheckStautsEnums(String status, String text) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
}
