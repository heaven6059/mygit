package com.yougou.logistics.city.common.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.enums.BaseInfoCategroyLevelEnums;
import com.yougou.logistics.city.common.enums.BaseInfoDisableAndEnabledEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class Category {
    private String cateNo;

    private String cateCode;

    private String cateName;

    private String searchCode;

    private String sysNo;

    private Short cateLevelid;

    private String headCateNo;

    private Short cateDepart;

    private String status;

    private String auditStatus;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String remarks;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String editor;

    private String headCateNoStr;

    private String sysNoStr;

    private int childrenCount;

    private String statusStr;

    private String cateLevelidStr;

    private String alarmrate;

    public String getAlarmrate() {
	return alarmrate;
    }

    public void setAlarmrate(String alarmrate) {
	this.alarmrate = alarmrate;
    }

    public String getFreezerate() {
	return freezerate;
    }

    public void setFreezerate(String freezerate) {
	this.freezerate = freezerate;
    }

    public Date getRecievetm() {
	return recievetm;
    }

    public void setRecievetm(Date recievetm) {
	this.recievetm = recievetm;
    }

    private String freezerate;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date recievetm;

    public String getCateNo() {
	return cateNo;
    }

    public void setCateNo(String cateNo) {
	this.cateNo = cateNo;
    }

    public String getCateCode() {
	return cateCode;
    }

    public void setCateCode(String cateCode) {
	this.cateCode = cateCode;
    }

    public String getCateName() {
	return cateName;
    }

    public void setCateName(String cateName) {
	this.cateName = cateName;
    }

    public String getSearchCode() {
	return searchCode;
    }

    public void setSearchCode(String searchCode) {
	this.searchCode = searchCode;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNo = sysNo;
    }

    public Short getCateLevelid() {
	return cateLevelid;
    }

    public void setCateLevelid(Short cateLevelid) {
	this.cateLevelid = cateLevelid;
	this.cateLevelidStr = BaseInfoCategroyLevelEnums.getTextByLevels(String
		.valueOf(this.cateLevelid));
    }

    public String getHeadCateNo() {
	return headCateNo;
    }

    public void setHeadCateNo(String headCateNo) {
	this.headCateNo = headCateNo;
    }

    public Short getCateDepart() {
	return cateDepart;
    }

    public void setCateDepart(Short cateDepart) {
	this.cateDepart = cateDepart;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
	this.statusStr = BaseInfoDisableAndEnabledEnums
		.getTextByStatus(this.status);
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

    public String getHeadCateNoStr() {
	return headCateNoStr;
    }

    public void setHeadCateNoStr(String headCateNoStr) {
	this.headCateNoStr = headCateNoStr;
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

    public String getCateLevelidStr() {
	return cateLevelidStr;
    }

    public void setCateLevelidStr(String cateLevelidStr) {
	this.cateLevelidStr = cateLevelidStr;
    }

}