package com.yougou.logistics.city.common.enums;
/**
 * 库区-库区属性
 * @author jiang.ys
 *
 */
public enum CmDefAreaAreaAttributeEnums {
	AREAAREAATTRIBUTE_0("0","作业区"),
	AREAAREAATTRIBUTE_1("1","暂存区"),
	AREAAREAATTRIBUTE_2("2","以配送区"),
	AREAAREAATTRIBUTE_3("3","问题区"),
	AREAAREAATTRIBUTE_4("4","虚拟区");
	private String areaareaAttribute;
	private String desc;

	private CmDefAreaAreaAttributeEnums(String areaareaAttribute, String desc) {
		this.areaareaAttribute = areaareaAttribute;
		this.desc = desc;
	}

	public String getAreaareaAttribute() {
		return this.areaareaAttribute;
	}

	public String getDesc() {
		return this.desc;
	}
}
