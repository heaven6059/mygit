/*
 * 类名 com.yougou.logistics.city.common.model.BillUmCheckTaskDtl
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillUmCheckTaskDtl extends BillUmCheckTaskDtlKey {
	private String itemNo;

	private String sizeNo;

	private BigDecimal itemQty;

	private BigDecimal checkQty;

	private String boxNo;

	private String status;

	private String brandNo;

	private String storeNo;

	private Integer packQty;

	private String itemType;

	private String quality;

	private String checkWorker;

	private Date checktm;

	private String checkWorkerName;

	private String addFlag;

	private BigDecimal difQty;
	
	private String itemName;
	
	private String colorName;
	
	private String brandName;
	
	private String storeName;

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

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public Integer getPackQty() {
		return packQty;
	}

	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getCheckWorker() {
		return checkWorker;
	}

	public void setCheckWorker(String checkWorker) {
		this.checkWorker = checkWorker;
	}

	public Date getChecktm() {
		return checktm;
	}

	public void setChecktm(Date checktm) {
		this.checktm = checktm;
	}

	public String getCheckWorkerName() {
		return checkWorkerName;
	}

	public void setCheckWorkerName(String checkWorkerName) {
		this.checkWorkerName = checkWorkerName;
	}

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public BigDecimal getDifQty() {
		return difQty;
	}

	public void setDifQty(BigDecimal difQty) {
		this.difQty = difQty;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}