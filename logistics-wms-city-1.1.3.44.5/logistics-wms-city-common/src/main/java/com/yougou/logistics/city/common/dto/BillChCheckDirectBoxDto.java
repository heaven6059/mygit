package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

/**
 * TODO: 增加描述
 * 
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillChCheckDirectBoxDto {

    private String boxNo;

    private String cellNo;

	private String quality;
	
	private String itemType;

	private BigDecimal receiptQty;

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public BigDecimal getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(BigDecimal receiptQty) {
		this.receiptQty = receiptQty;
	}

}
