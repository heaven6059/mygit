package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-10-16 11:05:09
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public class BillWmRecheckDtl extends BillWmRecheckDtlKey {
	private String ownerNo;

	private String itemNo;

	private Long itemId;

	private BigDecimal itemQty;

	private BigDecimal realQty;

	private String addFlag;

	private String status;

	private String assignName;

	private String recheckName;

	private Date recheckDate;

	private String recedeNo;

	private String recedeType;

	private Date recedeDate;

	private BigDecimal packQty;

	private String recheckName2;

	private String sizeNo;

	private String itemName;

	private String colorName;

	private String statusStr;

	private String scanLabelNo;

	private String cellNo;

	private BigDecimal cellId;
	
	private String brandNo;

	//指定复核人中文名称
	private String assignChName;
	//实际复核人中文名称
	private String recheckChName;
	
	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getRecheckName() {
		return recheckName;
	}

	public void setRecheckName(String recheckName) {
		this.recheckName = recheckName;
	}

	public Date getRecheckDate() {
		return recheckDate;
	}

	public void setRecheckDate(Date recheckDate) {
		this.recheckDate = recheckDate;
	}

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
	}

	public String getRecedeType() {
		return recedeType;
	}

	public void setRecedeType(String recedeType) {
		this.recedeType = recedeType;
	}

	public Date getRecedeDate() {
		return recedeDate;
	}

	public void setRecedeDate(Date recedeDate) {
		this.recedeDate = recedeDate;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getRecheckName2() {
		return recheckName2;
	}

	public void setRecheckName2(String recheckName2) {
		this.recheckName2 = recheckName2;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getScanLabelNo() {
		return scanLabelNo;
	}

	public void setScanLabelNo(String scanLabelNo) {
		this.scanLabelNo = scanLabelNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public BigDecimal getCellId() {
		return cellId;
	}

	public void setCellId(BigDecimal cellId) {
		this.cellId = cellId;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getAssignChName() {
		return assignChName;
	}

	public void setAssignChName(String assignChName) {
		this.assignChName = assignChName;
	}

	public String getRecheckChName() {
		return recheckChName;
	}

	public void setRecheckChName(String recheckChName) {
		this.recheckChName = recheckChName;
	}
	
}