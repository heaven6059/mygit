package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 装车出库尺码横排
 * @author jiang.ys
 * 
 */
public class BillOmDeliverDtlSizeDto {

	private String itemNo;
	private String sizeNo;
	private String storeNo;
	private String storeName;
	private BigDecimal qty;
	private BigDecimal totalQty;
	private String sizeKind;
	private String sizeCode;
	private String sysNo;
	private Map<String, BigDecimal> sizeCodeQtyMap;
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getSizeNo() {
		return sizeNo;
	}
	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	public String getSizeKind() {
		return sizeKind;
	}
	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}
	public String getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public Map<String, BigDecimal> getSizeCodeQtyMap() {
		return sizeCodeQtyMap;
	}
	public void setSizeCodeQtyMap(Map<String, BigDecimal> sizeCodeQtyMap) {
		this.sizeCodeQtyMap = sizeCodeQtyMap;
	}
	
}