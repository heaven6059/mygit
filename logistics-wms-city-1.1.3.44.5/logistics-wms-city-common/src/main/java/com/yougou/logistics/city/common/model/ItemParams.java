package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

import com.yougou.logistics.base.common.utils.SimplePage;

public class ItemParams extends SimplePage {

	private static final long serialVersionUID = 1L;

	private String sysNo;

	private String sysNOName;

	private String cellNo;

	private String itemNo;

	private String itemName;

	private String styleNo;

	private String barCode;

	private String size;

	private String color;

	private String colorName;

	private String areaNo;

	private String cellid;

	private String locNo;

	private String adjType;

	private String sItemType;
	
	private String stockType;
	
	private String stockValue;

	private String ownerNo;
	
	private BigDecimal qty;
	
	private BigDecimal outQty;
	
	private BigDecimal itemId;
	
	private String areaNoStr;
	
	private String quality;
	
	private String itemType;
	
	private BigDecimal packQty; 
	
	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSysNOName() {
		return sysNOName;
	}

	public void setSysNOName(String sysNOName) {
		this.sysNOName = sysNOName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getCellid() {
		return cellid;
	}

	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	public String getLocNo() {
		return locNo;
	}

	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}

	public String getAdjType() {
		return adjType;
	}

	public void setAdjType(String adjType) {
		this.adjType = adjType;
	}

	public String getsItemType() {
		return sItemType;
	}

	public void setsItemType(String sItemType) {
		this.sItemType = sItemType;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStockValue() {
		return stockValue;
	}

	public void setStockValue(String stockValue) {
		this.stockValue = stockValue;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getItemId() {
		return itemId;
	}

	public void setItemId(BigDecimal itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getOutQty() {
		return outQty;
	}

	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}

	public String getAreaNoStr() {
		return areaNoStr;
	}

	public void setAreaNoStr(String areaNoStr) {
		this.areaNoStr = areaNoStr;
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

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}
}
