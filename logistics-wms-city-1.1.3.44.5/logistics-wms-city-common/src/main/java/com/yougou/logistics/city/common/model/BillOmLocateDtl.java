/*
 * 类名 com.yougou.logistics.city.common.model.BillOmLocateDtl
 * @author su.yq
 * @date  Mon Nov 04 14:35:57 CST 2013
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

public class BillOmLocateDtl extends BillOmLocateDtlKey {
	private String storeNo;

	private String subStoreNo;

	private String expNo;

	private String itemNo;

	private String sizeNo;

	private BigDecimal planQty;

	private BigDecimal locatedQty;

	private String status;

	private String lineNo;

	private String batchNo;

	private String condition;

	private String specialBatch;

	private String bOutFlag;

	private Short priority;

	private String addExpNo;

	private String itemType;

	private Date expDate;

	private BigDecimal planExportQty;

	private BigDecimal exportQty;

	private String importNo;

	private String stockType;

	private String quality;

	private Short packQty;

	/**
	 * 用于界面显示
	 * @return
	 */
	
	private String statusStr;//状态显示
	
	private String storeName;//客户名称

	private String itemName;//商品名称

	private String colorName;//颜色
	
	private String cellNo;//来源储位
	
	private String selectType;//0 波次明细 1定位明细
	
	private BigDecimal outstockQty;//拣货数量
	
	private BigDecimal recheckQty;//复核数量
	
	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getSubStoreNo() {
		return subStoreNo;
	}

	public void setSubStoreNo(String subStoreNo) {
		this.subStoreNo = subStoreNo;
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

	public BigDecimal getPlanQty() {
		return planQty;
	}

	public void setPlanQty(BigDecimal planQty) {
		this.planQty = planQty;
	}

	public BigDecimal getLocatedQty() {
		return locatedQty;
	}

	public void setLocatedQty(BigDecimal locatedQty) {
		this.locatedQty = locatedQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSpecialBatch() {
		return specialBatch;
	}

	public void setSpecialBatch(String specialBatch) {
		this.specialBatch = specialBatch;
	}

	public String getbOutFlag() {
		return bOutFlag;
	}

	public void setbOutFlag(String bOutFlag) {
		this.bOutFlag = bOutFlag;
	}

	public Short getPriority() {
		return priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public String getAddExpNo() {
		return addExpNo;
	}

	public void setAddExpNo(String addExpNo) {
		this.addExpNo = addExpNo;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public BigDecimal getPlanExportQty() {
		return planExportQty;
	}

	public void setPlanExportQty(BigDecimal planExportQty) {
		this.planExportQty = planExportQty;
	}

	public BigDecimal getExportQty() {
		return exportQty;
	}

	public void setExportQty(BigDecimal exportQty) {
		this.exportQty = exportQty;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Short getPackQty() {
		return packQty;
	}

	public void setPackQty(Short packQty) {
		this.packQty = packQty;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public BigDecimal getOutstockQty() {
		return outstockQty;
	}

	public void setOutstockQty(BigDecimal outstockQty) {
		this.outstockQty = outstockQty;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}
}