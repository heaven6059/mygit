package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-17 20:35:58
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
public class BillUmInstockDtl extends BillUmInstockDtlKey {
	private Long directSerial;

	private String itemNo;

	private String itemName;

	private String colorName;

	private String sizeNo;

	private String cellNo;

	private Long cellId;

	private String destCellNo;

	private Long destCellId;

	private String realCellNo;

	private BigDecimal itemQty;

	private BigDecimal realQty;
	
	private BigDecimal instockedQty;

	private String boxNo;

	private Date instockDate;

	private String instockWorker;
	
	private String instockName;

	private String status;
	
	private String sourceNo;
	
	private String brandNo;

	private String itemType;
	
	private String quality;
	public Long getDirectSerial() {
		return directSerial;
	}

	public void setDirectSerial(Long directSerial) {
		this.directSerial = directSerial;
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

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Long getCellId() {
		return cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	public String getDestCellNo() {
		return destCellNo;
	}

	public void setDestCellNo(String destCellNo) {
		this.destCellNo = destCellNo;
	}

	public Long getDestCellId() {
		return destCellId;
	}

	public void setDestCellId(Long destCellId) {
		this.destCellId = destCellId;
	}

	public String getRealCellNo() {
		return realCellNo;
	}

	public void setRealCellNo(String realCellNo) {
		this.realCellNo = realCellNo;
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

	public BigDecimal getInstockedQty() {
		return instockedQty;
	}

	public void setInstockedQty(BigDecimal instockedQty) {
		this.instockedQty = instockedQty;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public Date getInstockDate() {
		return instockDate;
	}

	public void setInstockDate(Date instockDate) {
		this.instockDate = instockDate;
	}

	public String getInstockWorker() {
		return instockWorker;
	}

	public void setInstockWorker(String instockWorker) {
		this.instockWorker = instockWorker;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
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

	public String getInstockName() {
		return instockName;
	}

	public void setInstockName(String instockName) {
		this.instockName = instockName;
	}
	
}