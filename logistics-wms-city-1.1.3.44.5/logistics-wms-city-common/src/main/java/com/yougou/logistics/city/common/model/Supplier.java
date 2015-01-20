package com.yougou.logistics.city.common.model;

import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yougou.logistics.city.common.enums.BaseInfoStatusEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class Supplier {
	private String supplierNo;

	private String supplierCode;

	private String supplierName;

	private String supplierLname;

	private String supplierEname;

	private String searchCode;

	private String supplierNoHead;

	private Short supplierType;

	private Short businessType;

	private Short prepayFlag;

	private Short bizType;

	private String supplierCardNo;

	private String taxpayingNo;

	private String identityCard;

	private Short primaryFlag;

	private String sysNo;

	private String address;

	private String telno;

	private String faxno;

	private String cman;

	private String cmanPhone;

	private String phone;

	private String manager;

	private String zipno;

	private String bankName;

	private String bankAccount;

	private String bankAccname;

	private String bizLevel;

	private String chairman;

	private String zoneNo;

	private Short taxLevel;

	private String supplierStatus;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String remarks;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String editor;

	private String recievetm;

	private String sysNoStr;

	private String supplierTypeStr;

	private String businessTypeStr;

	private String zoneNoStr;

	private String supplierNoHeadStr;

	private String taxLevelStr;

	private String supplierStatusStr;

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierLname() {
		return supplierLname;
	}

	public void setSupplierLname(String supplierLname) {
		this.supplierLname = supplierLname;
	}

	public String getSupplierEname() {
		return supplierEname;
	}

	public void setSupplierEname(String supplierEname) {
		this.supplierEname = supplierEname;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getSupplierNoHead() {
		return supplierNoHead;
	}

	public void setSupplierNoHead(String supplierNoHead) {
		this.supplierNoHead = supplierNoHead;
	}

	public Short getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(Short supplierType) {
		this.supplierType = supplierType;
	}

	public Short getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Short businessType) {
		this.businessType = businessType;
	}

	public Short getPrepayFlag() {
		return prepayFlag;
	}

	public void setPrepayFlag(Short prepayFlag) {
		this.prepayFlag = prepayFlag;
	}

	public Short getBizType() {
		return bizType;
	}

	public void setBizType(Short bizType) {
		this.bizType = bizType;
	}

	public String getSupplierCardNo() {
		return supplierCardNo;
	}

	public void setSupplierCardNo(String supplierCardNo) {
		this.supplierCardNo = supplierCardNo;
	}

	public String getTaxpayingNo() {
		return taxpayingNo;
	}

	public void setTaxpayingNo(String taxpayingNo) {
		this.taxpayingNo = taxpayingNo;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public Short getPrimaryFlag() {
		return primaryFlag;
	}

	public void setPrimaryFlag(Short primaryFlag) {
		this.primaryFlag = primaryFlag;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getFaxno() {
		return faxno;
	}

	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getCman() {
		return cman;
	}

	public void setCman(String cman) {
		this.cman = cman;
	}

	public String getCmanPhone() {
		return cmanPhone;
	}

	public void setCmanPhone(String cmanPhone) {
		this.cmanPhone = cmanPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getZipno() {
		return zipno;
	}

	public void setZipno(String zipno) {
		this.zipno = zipno;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankAccname() {
		return bankAccname;
	}

	public void setBankAccname(String bankAccname) {
		this.bankAccname = bankAccname;
	}

	public String getBizLevel() {
		return bizLevel;
	}

	public void setBizLevel(String bizLevel) {
		this.bizLevel = bizLevel;
	}

	public String getChairman() {
		return chairman;
	}

	public void setChairman(String chairman) {
		this.chairman = chairman;
	}

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public Short getTaxLevel() {
		return taxLevel;
	}

	public void setTaxLevel(Short taxLevel) {
		this.taxLevel = taxLevel;
	}

	public String getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(String supplierStatus) {
		this.supplierStatus = supplierStatus;
		this.supplierStatusStr = BaseInfoStatusEnums
				.getTextByStatus(this.supplierStatus);
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}

	public String getSupplierTypeStr() {
		return supplierTypeStr;
	}

	public void setSupplierTypeStr(String supplierTypeStr) {
		this.supplierTypeStr = supplierTypeStr;
	}

	public String getBusinessTypeStr() {
		return businessTypeStr;
	}

	public void setBusinessTypeStr(String businessTypeStr) {
		this.businessTypeStr = businessTypeStr;
	}

	public String getZoneNoStr() {
		return zoneNoStr;
	}

	public void setZoneNoStr(String zoneNoStr) {
		this.zoneNoStr = zoneNoStr;
	}

	public String getSupplierNoHeadStr() {
		return supplierNoHeadStr;
	}

	public void setSupplierNoHeadStr(String supplierNoHeadStr) {
		this.supplierNoHeadStr = supplierNoHeadStr;
	}

	public String getTaxLevelStr() {
		return taxLevelStr;
	}

	public void setTaxLevelStr(String taxLevelStr) {
		this.taxLevelStr = taxLevelStr;
	}

	public String getSupplierStatusStr() {
		return supplierStatusStr;
	}

	public void setSupplierStatusStr(String supplierStatusStr) {
		this.supplierStatusStr = supplierStatusStr;
	}

	public String getRecievetm() {
		return recievetm;
	}

	public void setRecievetm(String recievetm) {
		this.recievetm = recievetm;
	}

}