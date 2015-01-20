package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class BillImImportDtlSizeKind extends BaseSizeType {

    private String importNo;

    private String itemNo;
    
    private String boxNo;
    
    private String originalBoxNo;//原箱号

    private String itemName;

    private String sizeNo;

    private String sysNo;

    private String sizeKind;
    
    private BigDecimal importQty;
    
    private BigDecimal  poQty;
    
    private String styleNo;
    
    private String  colorNoStr;//颜色
    
    private String  brandNoStr;//品牌
    
    private BigDecimal allCount;

    private BigDecimal allCost;
    
    private String deliverNo;

	public BigDecimal getPoQty() {
		return poQty;
	}

	public void setPoQty(BigDecimal poQty) {
		this.poQty = poQty;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public BigDecimal getAllCount() {
		return allCount;
	}

	public void setAllCount(BigDecimal allCount) {
		this.allCount = allCount;
	}

	public BigDecimal getAllCost() {
		return allCost;
	}

	public void setAllCost(BigDecimal allCost) {
		this.allCost = allCost;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
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

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public BigDecimal getImportQty() {
		return importQty;
	}

	public void setImportQty(BigDecimal importQty) {
		this.importQty = importQty;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorNoStr() {
		return colorNoStr;
	}

	public void setColorNoStr(String colorNoStr) {
		this.colorNoStr = colorNoStr;
	}

	public String getBrandNoStr() {
		return brandNoStr;
	}

	public void setBrandNoStr(String brandNoStr) {
		this.brandNoStr = brandNoStr;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getOriginalBoxNo() {
		return originalBoxNo;
	}

	public void setOriginalBoxNo(String originalBoxNo) {
		this.originalBoxNo = originalBoxNo;
	}
}