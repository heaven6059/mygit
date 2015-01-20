package com.yougou.logistics.city.common.enums;
/**
 * 其它入库状态
 * 
 * @author jiang.ys
 */
public enum BillSmOtherinStatusEnums {
	VALUE_10("10","新建"),
	VALUE_13("13","确认");
    private String value;
    private String text;

    private BillSmOtherinStatusEnums(String value, String text) {
	this.value = value;
	this.text = text;
    }

    public String getValue() {
	return this.value;
    }

    public String getText() {
	return this.text;
    }
}
