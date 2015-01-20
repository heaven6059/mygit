package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-10-16 10:44:50
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
public class BillWmDeliverDtl extends BillWmDeliverDtlKey {
	private String supplierNo;

	private String barcode;

	private BigDecimal packQty;

	private String lotNo;

	private Date produceDate;

	private Date expireDate;

	private String quality;

	private String importBatchNo;

	private BigDecimal itemQty;

	private BigDecimal realQty;

	private String recedeName;

	private Date recedeDate;

	private String itemType;

	private String batchSerialNo;

	private String boxNo;

	private String recheckNo;

	// private String locno;

	// private String supplierNo;

	private String status;

	private String sendFlag;

	private String creator;

	private String createtm;

	private String editor;

	private String edittm;

	private String printStatus;

	private String serialNo;

	// private String recedeDate;

	private String auditor;

	private String audittm;

	private String divideNo;

	private String sumQty;

	private String labelNo;

	private String itemName;

	private String styleNo;

	private String sizeNo;

	private String colorNoStr;

	private String containerNo;

	private String supplierName;
	
	private String brandNo;
	
	private String recedeChName;
	
	public String getRecedeChName() {
		return recedeChName;
	}

	public void setRecedeChName(String recedeChName) {
		this.recedeChName = recedeChName;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getColorNoStr() {
		return colorNoStr;
	}

	public void setColorNoStr(String colorNoStr) {
		this.colorNoStr = colorNoStr;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	// public String getLocno() {
	// return locno;
	// }
	//
	// public void setLocno(String locno) {
	// this.locno = locno;
	// }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatetm() {
		return createtm;
	}

	public void setCreatetm(String createtm) {
		this.createtm = createtm;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdittm() {
		return edittm;
	}

	public void setEdittm(String edittm) {
		this.edittm = edittm;
	}

	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAudittm() {
		return audittm;
	}

	public void setAudittm(String audittm) {
		this.audittm = audittm;
	}

	public String getDivideNo() {
		return divideNo;
	}

	public void setDivideNo(String divideNo) {
		this.divideNo = divideNo;
	}

	public String getSumQty() {
		return sumQty;
	}

	public void setSumQty(String sumQty) {
		this.sumQty = sumQty;
	}

	public String getRecheckNo() {
		return recheckNo;
	}

	public void setRecheckNo(String recheckNo) {
		this.recheckNo = recheckNo;
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

	public String getImportBatchNo() {
		return importBatchNo;
	}

	public void setImportBatchNo(String importBatchNo) {
		this.importBatchNo = importBatchNo;
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

	public String getRecedeName() {
		return recedeName;
	}

	public void setRecedeName(String recedeName) {
		this.recedeName = recedeName;
	}

	public Date getRecedeDate() {
		return recedeDate;
	}

	public void setRecedeDate(Date recedeDate) {
		this.recedeDate = recedeDate;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
}