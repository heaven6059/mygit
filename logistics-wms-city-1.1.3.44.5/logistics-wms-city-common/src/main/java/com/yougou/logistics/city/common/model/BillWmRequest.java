/*
 * 类名 com.yougou.logistics.city.common.model.BillWmRequest
 * @author yougoupublic
 * @date  Fri Mar 21 17:59:52 CST 2014
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

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

public class BillWmRequest extends BillWmRequestKey {
    private String requestType;

    private String status;

    private String creator;
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String auditor;
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date audittm;

    private String sourceNo;

    private String sourceType;

    private String sysNo;

    private String statusName;

    private String ownerName;

    private String requestTypeName;

    private String sourceTypeName;

    private String businessType;

    private String businessTypeName;
    
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

	public String getRequestType() {
	return requestType;
    }

    public void setRequestType(String requestType) {
	this.requestTypeName = SystemCache.getLookUpName("CITY_RECEDE_TYPE",
		requestType);
	this.requestType = requestType;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.statusName = SystemCache.getLookUpName("CITY_WM_REQUEST_STATUS",
		status);
	this.status = status;
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

    public String getSourceNo() {
	return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
	this.sourceNo = sourceNo;
    }

    public String getSourceType() {
	return sourceType;
    }

    public void setSourceType(String sourceType) {

	this.sourceTypeName = SystemCache.getLookUpName("CITY_RECEDE_TYPE",
		sourceType);
	this.sourceType = sourceType;
    }

    public String getStatusName() {
	return statusName;
    }

    public void setStatusName(String statusName) {
	this.statusName = statusName;
    }

    public String getOwnerName() {
	return ownerName;
    }

    public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
    }

    public String getRequestTypeName() {
	return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
	this.requestTypeName = requestTypeName;
    }

    public String getSourceTypeName() {
	return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
	this.sourceTypeName = sourceTypeName;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNo = sysNo;
    }

    public String getBusinessType() {
	return businessType;
    }

    public void setBusinessType(String businessType) {
	this.businessTypeName = SystemCache.getLookUpName(
		"CITY_WM_BUSINESS_TYPE", businessType);
	this.businessType = businessType;
    }

    public String getBusinessTypeName() {
	return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
	this.businessTypeName = businessTypeName;
    }

}