package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-7-14 下午3:18:00
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class InoutDiffReportDto {
	private Date poDate;//日期
	private String billNo;//来源单据
	private String billType;//单据类型
	private String billTypeName;//单据类型名称
	private String status;//状态
	private String statusName;//状态名称
	private String bizType;//业务类型
	private String bizTypeName;//业务类型名称
	private String storeNo;//门店编码
	private String storeName;//门店名称
	private String receiptNo;//操作单据(收货/拣货)
	private String receiptType;//操作单据类型(收货/拣货)
	private String receiptTypeName;//操作单据类型名称(收货/拣货)
	private String checkNo;//操作单据(验收/复核)
	private String checkType;//操作单据类型(验收/复核)
	private String checkTypeName;//操作单据类型名称(验收/复核)
	private String boxNo;//来源箱号
	private String itemNo;//商品编码
	private String sizeNo;//尺码
	private BigDecimal itemQty;//计划数量
	private BigDecimal receiptQty;//数量(收货/拣货)
	private BigDecimal checkQty;//数量（验收/复核）
	private BigDecimal diffQty;//差异数量
	private String operor;//操作人员
	private String operorName;//操作人员名称
	private Date opertm;//操作时间

	@JsonSerialize(using = JsonDateSerializer$10.class)
	public Date getPoDate() {
		return poDate;
	}
	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public String getBoxNo() {
		return boxNo;
	}
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
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
	public BigDecimal getItemQty() {
		return itemQty;
	}
	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}
	
	public BigDecimal getDiffQty() {
		return diffQty;
	}
	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
	}
	public String getOperor() {
		return operor;
	}
	public void setOperor(String operor) {
		this.operor = operor;
	}

	@JsonSerialize(using = JsonDateSerializer$10.class)
	public Date getOpertm() {
		return opertm;
	}
	public void setOpertm(Date opertm) {
		this.opertm = opertm;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public String getReceiptTypeName() {
		return receiptTypeName;
	}
	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(String checkTypeName) {
		this.checkTypeName = checkTypeName;
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
	public String getOperorName() {
		return operorName;
	}
	public void setOperorName(String operorName) {
		this.operorName = operorName;
	}
	
}
