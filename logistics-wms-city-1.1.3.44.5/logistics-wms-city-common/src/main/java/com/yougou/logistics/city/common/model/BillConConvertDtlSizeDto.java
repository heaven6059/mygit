package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Map;

public class BillConConvertDtlSizeDto {

	private BigDecimal totalQty;
	private String sizeCode;
	private String locno;
	private String ownerNo;
	private String converNo;
	private String itemNo;
	private String itemName;
	private String brandNo;
	private String sizeKind;
	private String sysNo;
	private BigDecimal ItemQty;
	private Map<String, BigDecimal> sizeCodeQtyMap;
	public BigDecimal getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	public String getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
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
	public String getConverNo() {
		return converNo;
	}
	public void setConverNo(String converNo) {
		this.converNo = converNo;
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
	public BigDecimal getItemQty() {
		return ItemQty;
	}
	public void setItemQty(BigDecimal itemQty) {
		ItemQty = itemQty;
	}
	public Map<String, BigDecimal> getSizeCodeQtyMap() {
		return sizeCodeQtyMap;
	}
	public void setSizeCodeQtyMap(Map<String, BigDecimal> sizeCodeQtyMap) {
		this.sizeCodeQtyMap = sizeCodeQtyMap;
	}
	
	
}
