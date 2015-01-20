package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

public class BillChDifferentDtl extends BillChDifferentDtlKey {
    private String ownerNo;

    private BigDecimal itemQty;

    private BigDecimal realQty;

    private String stockType;

    private String sizeNo;

    private BigDecimal rowId;

    private String itemName;

    private String colorName;

    private BigDecimal diffQty;

    private String brandNo;
    
    private String brandName;

    public String getOwnerNo() {
	return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
	this.ownerNo = ownerNo;
    }

    public BigDecimal getItemQty() {
	return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
	this.itemQty = itemQty;
    }

    public BigDecimal getRealQty() {
	return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
	this.realQty = realQty;
    }

    public String getStockType() {
	return stockType;
    }

    public void setStockType(String stockType) {
	this.stockType = stockType;
    }

    public String getSizeNo() {
	return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
	this.sizeNo = sizeNo;
    }

    public BigDecimal getRowId() {
	return rowId;
    }

    public void setRowId(BigDecimal rowId) {
	this.rowId = rowId;
    }

    public String getItemName() {
	return itemName;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public String getColorName() {
	return colorName;
    }

    public void setColorName(String colorName) {
	this.colorName = colorName;
    }

    public BigDecimal getDiffQty() {
	return diffQty;
    }

    public void setDiffQty(BigDecimal diffQty) {
	this.diffQty = diffQty;
    }

    public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getBrandName() {
	return brandName;
    }

    public void setBrandName(String brandName) {
	this.brandName = brandName;
    }
}