package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillChDifferent extends BillChDifferentKey {
    private String ownerNo;

    private String planNo;

    private String requestNo;

    private String checkNo;

    private String status;

    private String differentRemark;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private BigDecimal statusTrans;

    private String storeNo;

    private String operatorWorker;

    private String sysNo;

    private String auditor;

    private Date audittm;
    
    private String creatorName;
    
    private String editorName;
    
    private String auditorName;

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDifferentRemark() {
        return differentRemark;
    }

    public void setDifferentRemark(String differentRemark) {
        this.differentRemark = differentRemark;
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

    public BigDecimal getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(BigDecimal statusTrans) {
        this.statusTrans = statusTrans;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getOperatorWorker() {
        return operatorWorker;
    }

    public void setOperatorWorker(String operatorWorker) {
        this.operatorWorker = operatorWorker;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
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
    
    
}