package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.BillImInstockDtl;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-9-30 下午3:42:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillImInstockDtlDto extends BillImInstockDtl {
	//商品颜色
	private String colorName;
	//商品名称
	private String itemName;
	//款号
	private String styleNo;

	private String sizeKind;

	private String sysNo;

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

}
