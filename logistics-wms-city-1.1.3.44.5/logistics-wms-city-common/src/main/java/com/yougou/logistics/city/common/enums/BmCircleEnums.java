package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-11-22 下午4:18:35
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum BmCircleEnums {

	ENABLED("0","手建"),
	DISABLED("1","系统下发");
	
	private String createFlag;
	private String text;
	
	private BmCircleEnums(String createFlag, String text) {
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
		BmCircleEnums[] statusArr = BmCircleEnums.values();
		for (BmCircleEnums bmCircleEnums : statusArr) {
			if (bmCircleEnums.getCreateFlag().equals(createFlag)) {
				return bmCircleEnums.getText();
			}
		}
		return createFlag;
	}
}
