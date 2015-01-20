package com.yougou.logistics.city.common.enums;

/**
 * 盘点传输状态
 * 
 * @author luo.hl
 * @date 2013-12-18 下午4:15:44
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillChCheckStautsTransEnums {
	STAUTSTRANS10("10", "初始"), STAUTSTRANS11("11", "已上传");
	private String stautsTrans;

	private BillChCheckStautsTransEnums(String stautsTrans, String text) {
		this.stautsTrans = stautsTrans;
	}

	public String getStautsTrans() {
		return this.stautsTrans;
	}
}
