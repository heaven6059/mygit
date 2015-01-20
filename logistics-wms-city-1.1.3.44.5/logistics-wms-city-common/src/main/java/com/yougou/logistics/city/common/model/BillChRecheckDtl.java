/*
 * 类名 com.yougou.logistics.city.common.model.BillChRecheckDtl
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

public class BillChRecheckDtl extends BillChRecheckDtlKey {
	private String planNo;

	private String ownerNo;

	private String checkNo;

	private String cellNo;

	private String containerNo;

	private String itemNo;

	private String barcode;

	private BigDecimal packQty;

	private String lotNo;

	private Date produceDate;

	private Date expireDate;

	private String quality;

	private BigDecimal itemQty;

	private BigDecimal recheckQty;

	private BigDecimal diffQty;

	private String addFlag;

	private String status;

	private String reserved1;

	private String reserved2;

	private String reserved3;

	private String reserved4;

	private String checkWorker;

	private String itemType;

	private String labelNo;

	private String stockType;

	private String stockValue;

	private String batchSerialNo;

	private Short recheckTime;

	private Integer checkRowId;

	private String sizeNo;

	private String itemName;

	private String styleName;

	private String colorName;

	private String styleNo;

	private String differentFlag;
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date checkDate;

	private String sourceStatus;

	private String brandNo;
	
	private String fBoxFlag;

	private String checkWorkerName;
	
	public String getfBoxFlag() {
		return fBoxFlag;
	}

	public void setfBoxFlag(String fBoxFlag) {
		this.fBoxFlag = fBoxFlag;
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public BigDecimal getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
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

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getCheckWorker() {
		return checkWorker;
	}

	public void setCheckWorker(String checkWorker) {
		this.checkWorker = checkWorker;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
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

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public Short getRecheckTime() {
		return recheckTime;
	}

	public void setRecheckTime(Short recheckTime) {
		this.recheckTime = recheckTime;
	}

	public Integer getCheckRowId() {
		return checkRowId;
	}

	public void setCheckRowId(Integer checkRowId) {
		this.checkRowId = checkRowId;
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

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getDifferentFlag() {
		return differentFlag;
	}

	public void setDifferentFlag(String differentFlag) {
		this.differentFlag = differentFlag;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getCheckWorkerName() {
		return checkWorkerName;
	}

	public void setCheckWorkerName(String checkWorkerName) {
		this.checkWorkerName = checkWorkerName;
	}

}