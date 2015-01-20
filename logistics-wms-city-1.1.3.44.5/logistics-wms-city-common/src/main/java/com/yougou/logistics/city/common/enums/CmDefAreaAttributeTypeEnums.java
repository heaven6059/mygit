package com.yougou.logistics.city.common.enums;
/**
 * 库区-属性类型
 * @author jiang.ys
 *
 */
public enum CmDefAreaAttributeTypeEnums {
	ATTRIBUTETYPE_0("0","存储区"),
	ATTRIBUTETYPE_1("1","进货"),
	ATTRIBUTETYPE_2("2","出货整理"),
	ATTRIBUTETYPE_3("3","出货复核"),
	ATTRIBUTETYPE_4("4","出货滑道"),
	ATTRIBUTETYPE_5("5","发货"),
	ATTRIBUTETYPE_6("6","退货"),
	ATTRIBUTETYPE_7("7","报损"),
	ATTRIBUTETYPE_8("8","直通"),
	ATTRIBUTETYPE_9("9","电子标签");
	private String attributeType;
	private String desc;

	private CmDefAreaAttributeTypeEnums(String attributeType, String desc) {
		this.attributeType = attributeType;
		this.desc = desc;
	}

	public String getAttributeType() {
		return this.attributeType;
	}

	public String getDesc() {
		return this.desc;
	}
}
