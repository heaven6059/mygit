package com.yougou.logistics.city.common.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-14 下午5:14:17
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillWmOutstockDtlDto extends BillWmOutstockDtl {
	private String itemName;
	private String styleNo;
	private String colorNo;
	private String colorName;
    private int diffQty;
    private String barcode;
    private String recedeType;
    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date recedeDate;
    private String brandNo;

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

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public int getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(int diffQty) {
		this.diffQty = diffQty;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	public String getRecedeType() {
		return recedeType;
	}

	public void setRecedeType(String recedeType) {
		this.recedeType = recedeType;
	}

	public Date getRecedeDate() {
		return recedeDate;
	}

	public void setRecedeDate(Date recedeDate) {
		this.recedeDate = recedeDate;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
}
