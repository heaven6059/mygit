/*
 * 类名 com.yougou.logistics.city.common.model.BillImReceipt
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillImReceipt extends BillImReceiptKey {
	private String receiptType;

	private String storeNoFrom;

	private String ownerName;

	private String dockNo;
	
	private String dockName;

	private String receiptWorker;

	private String carPlate;

	private String shipDriver;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date receiptStartDate;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date receiptEndDate;

	private String printerGroupNo;

	private BigDecimal printTimes;

	private String status;
	
	private String uptStatus;

	private String statusTrans;

	private String remark;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date recivedate;

	private String transNo;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date edittm;

	private String auditor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;
	
	private String businessType;

	private BigDecimal totalBoxQty;//总箱数

	private BigDecimal totalReciveQty;//总件数

	private String importNo;//预通知单号

	private String startReciveDate;//起始收货日期

	private String endReciveDate;//结束收货日期

	private String storeNoFromStr;//供应商

	private String supplierNo;//供应商
	
	private String supplierName;

	private String barcode;

	private String sysNo;
	
	private String deliverNo;
	
	private BigDecimal receiptqty;
	
	private BigDecimal boxqty;
	
	private String brandNo;
	
	private String checkStatus;
	
	private String receiptName;//收货人姓名
	private String creatorName;//创建人姓名
	private String editorName;//编辑人姓名
	private String auditorName;//审核人姓名
	
	
	
	public String getReceiptName() {
		return receiptName;
	}
	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getUptStatus() {
		return uptStatus;
	}

	public void setUptStatus(String uptStatus) {
		this.uptStatus = uptStatus;
	}

	public String getDockName() {
		return dockName;
	}

	public void setDockName(String dockName) {
		this.dockName = dockName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public BigDecimal getReceiptqty() {
		return receiptqty;
	}

	public void setReceiptqty(BigDecimal receiptqty) {
		this.receiptqty = receiptqty;
	}

	public BigDecimal getBoxqty() {
		return boxqty;
	}

	public void setBoxqty(BigDecimal boxqty) {
		this.boxqty = boxqty;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public String getStoreNoFrom() {
		return storeNoFrom;
	}

	public void setStoreNoFrom(String storeNoFrom) {
		this.storeNoFrom = storeNoFrom;
	}

	public String getDockNo() {
		return dockNo;
	}

	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}

	public String getReceiptWorker() {
		return receiptWorker;
	}

	public void setReceiptWorker(String receiptWorker) {
		this.receiptWorker = receiptWorker;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public String getShipDriver() {
		return shipDriver;
	}

	public void setShipDriver(String shipDriver) {
		this.shipDriver = shipDriver;
	}

	public Date getReceiptStartDate() {
		return receiptStartDate;
	}

	public void setReceiptStartDate(Date receiptStartDate) {
		this.receiptStartDate = receiptStartDate;
	}

	public Date getReceiptEndDate() {
		return receiptEndDate;
	}

	public void setReceiptEndDate(Date receiptEndDate) {
		this.receiptEndDate = receiptEndDate;
	}

	public String getPrinterGroupNo() {
		return printerGroupNo;
	}

	public void setPrinterGroupNo(String printerGroupNo) {
		this.printerGroupNo = printerGroupNo;
	}

	public BigDecimal getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(BigDecimal printTimes) {
		this.printTimes = printTimes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusTrans() {
		return statusTrans;
	}

	public void setStatusTrans(String statusTrans) {
		this.statusTrans = statusTrans;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getRecivedate() {
		return recivedate;
	}

	public void setRecivedate(Date recivedate) {
		this.recivedate = recivedate;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAudittm() {
		return audittm;
	}

	public void setAudittm(Date audittm) {
		this.audittm = audittm;
	}

	public BigDecimal getTotalBoxQty() {
		return totalBoxQty;
	}

	public void setTotalBoxQty(BigDecimal totalBoxQty) {
		this.totalBoxQty = totalBoxQty;
	}

	public BigDecimal getTotalReciveQty() {
		return totalReciveQty;
	}

	public void setTotalReciveQty(BigDecimal totalReciveQty) {
		this.totalReciveQty = totalReciveQty;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public String getStartReciveDate() {
		return startReciveDate;
	}

	public void setStartReciveDate(String startReciveDate) {
		this.startReciveDate = startReciveDate;
	}

	public String getEndReciveDate() {
		return endReciveDate;
	}

	public void setEndReciveDate(String endReciveDate) {
		this.endReciveDate = endReciveDate;
	}

	public String getStoreNoFromStr() {
		return storeNoFromStr;
	}

	public void setStoreNoFromStr(String storeNoFromStr) {
		this.storeNoFromStr = storeNoFromStr;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
}