package com.yougou.logistics.city.common.enums;

/**
 * 品质
 * 
 * @author jiang.ys
 */
public enum QualityEnums {
	QUALITY0("0", "正品"), 
	QUALITYA("A", "次品");
	private String value;
	private String desc;

	private QualityEnums(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return this.value;
	}

	public String getDesc() {
		return this.desc;
	}
	
	public static String getValueByDesc(String desc){
		QualityEnums[] qe = values();
		for(QualityEnums q:qe){
			if(q.getDesc().equals(desc)){
				return q.getValue();
			}
		}
		return null;
	}
}
