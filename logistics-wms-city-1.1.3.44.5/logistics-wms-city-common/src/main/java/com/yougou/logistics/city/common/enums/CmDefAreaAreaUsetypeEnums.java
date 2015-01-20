package com.yougou.logistics.city.common.enums;
/**
 * 库区-库区用途
 * @author jiang.ys
 *
 */
public enum CmDefAreaAreaUsetypeEnums {
	AREAUSETYPE_1("1","普通存储区"),
	AREAUSETYPE_2("2","报损区"),
	AREAUSETYPE_3("3","退货区"),
	AREAUSETYPE_4("4","虚拟区"),
	AREAUSETYPE_5("5","异常区"),
	AREAUSETYPE_6("6","贵重品区"),
	AREAUSETYPE_7("7","库存调整区");
	private String areaUsetype;
	private String desc;

	private CmDefAreaAreaUsetypeEnums(String areaUsetype, String desc) {
		this.areaUsetype = areaUsetype;
		this.desc = desc;
	}

	public String getAreaUsetype() {
		return this.areaUsetype;
	}

	public String getDesc() {
		return this.desc;
	}
}
