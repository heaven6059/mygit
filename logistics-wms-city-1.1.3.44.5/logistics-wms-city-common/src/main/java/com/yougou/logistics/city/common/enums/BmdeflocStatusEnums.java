package com.yougou.logistics.city.common.enums;

/**
 *仓别创建方式枚举类
 * @author zuo.sw
 *
 */
public enum BmdeflocStatusEnums {
	
	ENABLED("0","手建"),
	DISABLED("1","系统下发");
	
	private String createFlag;
	private String text;
	
	private BmdeflocStatusEnums(String createFlag, String text) {
		this.createFlag = createFlag;
		this.text = text;
	}

	public String getCreateFlag() {
		return createFlag;
	}

	public String getText() {
		return text;
	}
	
	public static String getTextByStatus(String createFlag) {
		BmdeflocStatusEnums[] statusArr = BmdeflocStatusEnums.values();
		for (BmdeflocStatusEnums baseInfoStatusEnums : statusArr) {
			if (baseInfoStatusEnums.getCreateFlag().equals(createFlag)) {
				return baseInfoStatusEnums.getText();
			}
		}
		return createFlag;
	}
	
	
	
	

}
