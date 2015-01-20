package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-1-4 上午11:53:50
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillHmPlanStatusEnums {
	STATUS10("10", "建单"), STATUS11("11", "已审核"), STATUS20("20", "已发单"), STATUS25("25", "部分下架"), STATUS30("30", "下架完成"), STATUS35(
			"35", "部分上架"), STATUS55("55", "上架完成"), STATUS90("90", "关闭");
	private String status;

	private BillHmPlanStatusEnums(String status, String desc) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
}
