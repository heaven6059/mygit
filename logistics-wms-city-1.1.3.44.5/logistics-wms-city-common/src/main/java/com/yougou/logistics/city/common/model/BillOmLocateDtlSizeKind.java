package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.dto.BaseSizeType;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-5-29 上午11:17:04
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillOmLocateDtlSizeKind extends BaseSizeType {
	private String storeNo;
	private String storeName;
	private String locateNo;
	private String expNo;
	private String itemNo;
	private String sizeNo;
	private String sysNo;
	private String sizeKind;
	private BigDecimal planQty;
	private BigDecimal realQty;
	private BigDecimal recheckQty;
	private BigDecimal allCount;

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

	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}

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

	public BigDecimal getAllCount() {
		return allCount;
	}

	public void setAllCount(BigDecimal allCount) {
		this.allCount = allCount;
	}

	public BigDecimal getPlanQty() {
		return planQty;
	}

	public void setPlanQty(BigDecimal planQty) {
		this.planQty = planQty;
	}

	public String getLocateNo() {
		return locateNo;
	}

	public void setLocateNo(String locateNo) {
		this.locateNo = locateNo;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

}
