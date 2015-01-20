package com.yougou.logistics.city.common.enums;

/**
 * 验收单据来源类型
 * 
 * @author luo.hl
 * @date 2014-1-14 上午11:12:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BillImCheckSourceTypeEnums {
	TYPE0("0", "正常单据"), TYPE1("1", "验收转单");
	private String type;
	private String desc;

	private BillImCheckSourceTypeEnums(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return this.type;
	}

	public String getDesc() {
		return this.desc;
	}
}
