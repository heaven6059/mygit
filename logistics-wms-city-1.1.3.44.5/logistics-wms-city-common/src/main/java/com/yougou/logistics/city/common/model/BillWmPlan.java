/*
 * 类名 com.yougou.logistics.city.common.model.BillWmPlan
 * @author yougoupublic
 * @date  Fri Mar 21 14:04:26 CST 2014
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

public class BillWmPlan extends BillWmPlanKey {
    private String planType;

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

    private String sourceStatus;

    private String statusName;

    private String planTypeName;

    private String ownerName;

    private String sysNo;

    private String sysNoName;

    private int dtlCount;

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

	public String getPlanType() {
	return planType;
    }

    public void setPlanType(String planType) {
	this.planTypeName = SystemCache.getLookUpName("CITY_RECEDE_TYPE",
		planType);
	this.planType = planType;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.statusName = SystemCache.getLookUpName("CITY_WM_PLAN_STATUS",
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
	this.sourceType = sourceType;
    }

    public String getSourceStatus() {
	return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
	this.sourceStatus = sourceStatus;
    }

    public String getStatusName() {
	return statusName;
    }

    public void setStatusName(String statusName) {
	this.statusName = statusName;
    }

    public String getPlanTypeName() {
	return planTypeName;
    }

    public void setPlanTypeName(String planTypeName) {
	this.planTypeName = planTypeName;
    }

    public String getOwnerName() {
	return ownerName;
    }

    public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNoName = SystemCache.getLookUpName("SYS_NO", sysNo);
	this.sysNo = sysNo;
    }

    public String getSysNoName() {
	return sysNoName;
    }

    public void setSysNoName(String sysNoName) {
	this.sysNoName = sysNoName;
    }

    public int getDtlCount() {
	return dtlCount;
    }

    public void setDtlCount(int dtlCount) {
	this.dtlCount = dtlCount;
    }

}