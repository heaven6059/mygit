package com.yougou.logistics.city.common.enums;

/**
 * 商品属性
 * 
 * @author jiang.ys
 */
public enum ItemTypeEnums {
	ITEMTYPE0("0", "零售"), 
	ITEMTYPE1("1", "调货"), 
	ITEMTYPE2("2", "备用"), 
	ITEMTYPE3("3", "多品"), 
	ITEMTYPE4("4", "退货"), 
	ITEMTYPE5("5", "索赔"), 
	ITEMTYPE6("6", "召回"), 
	ITEMTYPE7("7", "团购"), 
	ITEMTYPE8("8", "工服"), 
	ITEMTYPE9("9", "批发"), 
	ITEMTYPE10("10", "批发储备"), 
	ITEMTYPE11("11", "批发退货"), 
	ITEMTYPE12("12", "批发召回"), 
	ITEMTYPE13("13", "自营退货"), 
	ITEMTYPE14("14", "自营召回"), 
	ITEMTYPE15("15", "样品"), 
	ITEMTYPE16("16", "其他"), 
	ITEMTYPE17("17", "旧货");
	private String value;
	private String desc;

	private ItemTypeEnums(String value, String desc) {
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
		ItemTypeEnums[] qe = values();
		for(ItemTypeEnums q:qe){
			if(q.getDesc().equals(desc)){
				return q.getValue();
			}
		}
		return null;
	}
}
