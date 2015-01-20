/*
 * 类名 com.yougou.logistics.city.common.model.BillConConvert
 * @author su.yq
 * @date  Thu Jun 05 10:47:51 CST 2014
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

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillConConvert extends BillConConvertKey {
    private String convertType;

    private String poNo;

    private String storeNo;

    private String status;
    
    private String updStatus;

    private String statusTrans;

    private String creator;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date edittm;

    private String auditor;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date audittm;

    private String remark;

    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date convertDate;
    
    private String sourceNo;
    
    private String sourceType;

    private BigDecimal itemQty;
    private String creatorName;
    private String editorName;
    private String auditorName;

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getConvertDate() {
        return convertDate;
    }

    public void setConvertDate(Date convertDate) {
        this.convertDate = convertDate;
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

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
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