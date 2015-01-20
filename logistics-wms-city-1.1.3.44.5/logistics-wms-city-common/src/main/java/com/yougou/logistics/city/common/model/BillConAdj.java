package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
/**
 * 请写出类的用途  
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public class BillConAdj extends BillConAdjKey {
    private String adjType;

    private String poNo;

    @JsonSerialize(using = JsonDateSerializer$10.class)
    private Date adjDate;

    private String status;
    
    private String updStatus;

    private String createFlag;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String statusTrans;

    private String auditor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date audittm;

    private String sysNo;

    private String storeNo;

    private String sItemType;

    private String dItemType;

    private String remark;

    private String statusReport;

    private String cellAdjFlag;
    
    private String sourceNo;
    
    private String sourceType;
    
    /**
     * 调整数量
     */
    private BigDecimal adjQty;
    
    private String creatorName;
    private String editorName;
    private String auditorName;
    public String getAdjType() {
        return adjType;
    }

    public void setAdjType(String adjType) {
        this.adjType = adjType;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Date getAdjDate() {
        return adjDate;
    }

    public void setAdjDate(Date adjDate) {
        this.adjDate = adjDate;
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

    public String getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
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

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getsItemType() {
        return sItemType;
    }

    public void setsItemType(String sItemType) {
        this.sItemType = sItemType;
    }

    public String getdItemType() {
        return dItemType;
    }

    public void setdItemType(String dItemType) {
        this.dItemType = dItemType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusReport() {
        return statusReport;
    }

    public void setStatusReport(String statusReport) {
        this.statusReport = statusReport;
    }

	public String getCellAdjFlag() {
		return cellAdjFlag;
	}

	public void setCellAdjFlag(String cellAdjFlag) {
		this.cellAdjFlag = cellAdjFlag;
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

	public BigDecimal getAdjQty() {
		return adjQty;
	}

	public void setAdjQty(BigDecimal adjQty) {
		this.adjQty = adjQty;
	}

	public String getUpdStatus() {
		return updStatus;
	}

	public void setUpdStatus(String updStatus) {
		this.updStatus = updStatus;
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