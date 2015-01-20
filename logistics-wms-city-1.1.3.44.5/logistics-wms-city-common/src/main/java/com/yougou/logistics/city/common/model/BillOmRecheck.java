package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * 
 * 分货复核单
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:17:42
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmRecheck extends BillOmRecheckKey {

	/**
	 * 线路编码
	 */
	private String lineNo;

	/**
	 * 客户编码
	 */
	private String storeNo;

	/**
	 * 子客户编码
	 */
	private String subStoreNo;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 传输标示
	 */
	private String sendFlag;

	private String creator;
	
	private String creatorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	
	private String editorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	/**
	 * 打印状态
	 */
	private String printStatus;

	/**
	 * 序列号
	 */
	private String serialNo;

	/**
	 * 出货日期
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date expDate;

	private String auditor;
	
	private String auditorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;
	
	private String businessType;

	private String divideNo;
	
	private String sourceType;

	private String recheckName;
	
	private String recheckType;
	
	private String statusStr;

	private String storeName;
	
	private String printStatusStr;
	
	private String brandNo;
	
	private BigDecimal itemQty;
	
	private BigDecimal realQty;
	
	private BigDecimal recheckQty;
	
	private BigDecimal packageQty;
	
	private String sourceTypeStr;
	
	private String boxNo;
	
	private String conNo;
	private String cellNo;
	private BigDecimal skuQty;
	private String supplierNo;
	
	private BigDecimal qty;
	private String divideType;
	private String divideTypeName;
	
	
	public String getConNo() {
		return conNo;
	}

	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public BigDecimal getSkuQty() {
		return skuQty;
	}

	public void setSkuQty(BigDecimal skuQty) {
		this.skuQty = skuQty;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getDivideNo() {
		return divideNo;
	}

	public void setDivideNo(String divideNo) {
		this.divideNo = divideNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

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

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
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

	public String getRecheckName() {
		return recheckName;
	}

	public void setRecheckName(String recheckName) {
		this.recheckName = recheckName;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPrintStatusStr() {
		return printStatusStr;
	}

	public void setPrintStatusStr(String printStatusStr) {
		this.printStatusStr = printStatusStr;
	}

	public String getRecheckType() {
		return recheckType;
	}

	public void setRecheckType(String recheckType) {
		this.recheckType = recheckType;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public BigDecimal getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(BigDecimal packageQty) {
		this.packageQty = packageQty;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceTypeStr() {
		return sourceTypeStr;
	}

	public void setSourceTypeStr(String sourceTypeStr) {
		this.sourceTypeStr = sourceTypeStr;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getDivideType() {
		return divideType;
	}

	public void setDivideType(String divideType) {
		this.divideType = divideType;
	}

	public String getDivideTypeName() {
		return divideTypeName;
	}

	public void setDivideTypeName(String divideTypeName) {
		this.divideTypeName = divideTypeName;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

	public String getAuditorname() {
		return auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}
	
}