package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Map;

public class BillConAdjDtlSizeDto {

    private BigDecimal totalQty;
    private String sizeCode;   
    private String locno;
    private String ownerNo;
    private String adjNo;
    private String itemNo;
    private String itemName;
    private String brandNo;
    private String sizeKind;
    private String sysNo;
    private String quality;
    private BigDecimal adjQty;
    private String itemType;
    private String qualityStr;
    private String itemTypeStr;
    private Map<String, BigDecimal> sizeCodeQtyMap;
    private String labelNo; 
    
    
    public String getLabelNo() {
		return labelNo;
	}
	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}
	public String getLocno() {
        return locno;
    }
    public void setLocno(String locno) {
        this.locno = locno;
    }
    public String getOwnerNo() {
        return ownerNo;
    }
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }
    public String getAdjNo() {
        return adjNo;
    }
    public void setAdjNo(String adjNo) {
        this.adjNo = adjNo;
    }
    
    public String getSizeCode() {
        return sizeCode;
    }
    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
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
    public String getBrandNo() {
        return brandNo;
    }
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
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
    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public BigDecimal getAdjQty() {
        return adjQty;
    }
    public void setAdjQty(BigDecimal adjQty) {
        this.adjQty = adjQty;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public Map<String, BigDecimal> getSizeCodeQtyMap() {
        return sizeCodeQtyMap;
    }
    public void setSizeCodeQtyMap(Map<String, BigDecimal> sizeCodeQtyMap) {
        this.sizeCodeQtyMap = sizeCodeQtyMap;
    }
    public BigDecimal getTotalQty() {
        return totalQty;
    }
    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }
    public String getQualityStr() {
        return qualityStr;
    }
    public void setQualityStr(String qualityStr) {
        this.qualityStr = qualityStr;
    }
    public String getItemTypeStr() {
        return itemTypeStr;
    }
    public void setItemTypeStr(String itemTypeStr) {
        this.itemTypeStr = itemTypeStr;
    }
    
}
