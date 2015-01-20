package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.enums.BaseInfoDisableAndEnabledEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Brand {
	private String brandNo;

	private String brandCode;

	private String searchCode;

	private String brandName;

	private String brandEname;

	private BigDecimal brandType;

	private BigDecimal brandClass;

	private String brandNoHead;

	private String sysNo;

	private String status;

	private String auditStatus;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String remarks;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String editor;

	private String brandClassStr;

	private String brandNoHeadStr;

	private String sysNoStr;

	private int childrenCount;

	private String statusStr;// 品牌状态str

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandEname() {
		return brandEname;
	}

	public void setBrandEname(String brandEname) {
		this.brandEname = brandEname;
	}

	public BigDecimal getBrandType() {
		return brandType;
	}

	public void setBrandType(BigDecimal brandType) {
		this.brandType = brandType;
	}

	public BigDecimal getBrandClass() {
		return brandClass;
	}

	public void setBrandClass(BigDecimal brandClass) {
		this.brandClass = brandClass;
	}

	public String getBrandNoHead() {
		return brandNoHead;
	}

	public void setBrandNoHead(String brandNoHead) {
		this.brandNoHead = brandNoHead;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusStr = BaseInfoDisableAndEnabledEnums.getTextByStatus(this.status);
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
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

	public String getBrandClassStr() {
		return brandClassStr;
	}

	public void setBrandClassStr(String brandClassStr) {
		this.brandClassStr = brandClassStr;
	}

	public String getBrandNoHeadStr() {
		return brandNoHeadStr;
	}

	public void setBrandNoHeadStr(String brandNoHeadStr) {
		this.brandNoHeadStr = brandNoHeadStr;
	}

	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

}