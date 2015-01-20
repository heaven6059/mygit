/*
 * 类名 com.yougou.logistics.city.common.model.BillUmUntreadDtl
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
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

public class BillUmUntreadDtl extends BillUmUntreadDtlKey {
	private String itemNo;

	private String sizeNo;

	private BigDecimal itemQty;

	private BigDecimal sumQty;

	private BigDecimal receiptQty;

	private BigDecimal checkQty;

	private String boxNo;

	private String addFlag;

	private String status;

	private String itemName;

	private String colorName;

	private String brandNo;

	private String brandName;

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

	public BigDecimal getSumQty() {
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}

	public BigDecimal getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(BigDecimal receiptQty) {
		this.receiptQty = receiptQty;
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

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

}