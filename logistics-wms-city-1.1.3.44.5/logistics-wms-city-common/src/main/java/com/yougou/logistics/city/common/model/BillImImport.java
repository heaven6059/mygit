/*
 * 类名 com.yougou.logistics.city.common.model.BillImImport
 * @author zuo.sw
 * @date  Mon Sep 23 10:07:38 CST 2013
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

public class BillImImport extends BillImImportKey {
	private String importType;

	private String poType;

	private String poNo;

	private BigDecimal sumQty;

	private String transNo;

	private String supplierNo;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date orderDate;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date requestDate;

	private String status;
	
	private String uptStatus;

	private String createFlag;

	private String importRemark;

	private Short endDate = 0;

	private BigDecimal receiveType;

	private String payTypeName;

	private String orderTypeName;

	private String layType;

	private String typeClass;

	private String errorStatus;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String sendFlag;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date orderEndDate;

	private String withCodeFlag;

	private String stockType;

	private String stockValue;

	private String quality;

	private Short sampleRate;

	private String sysNo;

	private String sysName;

	private String supplierName;

	private String coNo;

	private String auditor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	private String sPoNo;

	private String ownerName;

	private String businessType;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date transTime;

	private BigDecimal boxNoQty;

	private BigDecimal receiptBoxNoQty;
	
	private BigDecimal receiptQty;

    private BigDecimal importQty;
    
    private BigDecimal differQty;

    private String creatorName;
    private String editorName;
    private String auditorName;
    
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

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getPoType() {
		return poType;
	}

	public void setPoType(String poType) {
		this.poType = poType;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
	}

	public String getImportRemark() {
		return importRemark;
	}

	public void setImportRemark(String importRemark) {
		this.importRemark = importRemark;
	}

	public Short getEndDate() {
		return endDate;
	}

	public void setEndDate(Short endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(BigDecimal receiveType) {
		this.receiveType = receiveType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getLayType() {
		return layType;
	}

	public void setLayType(String layType) {
		this.layType = layType;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
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

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public Date getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getWithCodeFlag() {
		return withCodeFlag;
	}

	public void setWithCodeFlag(String withCodeFlag) {
		this.withCodeFlag = withCodeFlag;
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Short getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(Short sampleRate) {
		this.sampleRate = sampleRate;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getCoNo() {
		return coNo;
	}

	public void setCoNo(String coNo) {
		this.coNo = coNo;
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

	public String getsPoNo() {
		return sPoNo;
	}

	public void setsPoNo(String sPoNo) {
		this.sPoNo = sPoNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public BigDecimal getSumQty() {
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}

	public BigDecimal getBoxNoQty() {
		return boxNoQty;
	}

	public void setBoxNoQty(BigDecimal boxNoQty) {
		this.boxNoQty = boxNoQty;
	}

	public BigDecimal getReceiptBoxNoQty() {
		return receiptBoxNoQty;
	}

	public void setReceiptBoxNoQty(BigDecimal receiptBoxNoQty) {
		this.receiptBoxNoQty = receiptBoxNoQty;
	}

	public BigDecimal getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(BigDecimal receiptQty) {
		this.receiptQty = receiptQty;
	}

	public BigDecimal getImportQty() {
		return importQty;
	}

	public void setImportQty(BigDecimal importQty) {
		this.importQty = importQty;
	}

	public BigDecimal getDifferQty() {
		return differQty;
	}

	public void setDifferQty(BigDecimal differQty) {
		this.differQty = differQty;
	}

}