package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.Item;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-12-19 下午5:52:39
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class ItemDto extends Item {
	private String colorName;
	private String brandName;
	private String sizeNo;

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

}
