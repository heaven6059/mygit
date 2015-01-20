package com.yougou.logistics.city.common.enums;
/**
 * 其它入库类型
 * 
 * @author jiang.ys
 */
public enum BillSmOtherinTypeEnums {
	VALUE_0("0","退厂入库"),
	VALUE_1("1","仓出"),
	VALUE_4("4","移仓"),
	VALUE_5("5","部门转货");
    private String value;
    private String text;

    private BillSmOtherinTypeEnums(String value, String text) {
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
