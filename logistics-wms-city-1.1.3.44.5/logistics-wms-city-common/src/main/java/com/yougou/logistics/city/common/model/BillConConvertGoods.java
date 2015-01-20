/*
 * 类名 com.yougou.logistics.city.common.model.BillConConvertGoods
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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

public class BillConConvertGoods extends BillConConvertGoodsKey {
    private String convertType;

    private String status;

    private String updStatus;
    
    private String storeNo;

    private String sQuality;

    private String dQuality;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String auditor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date audittm;

    private String remark;
    
    private String dLocno;

    private String creatorName;

    private String editorName;

    private String auditorName;

    private String businessType;
    
    private String storeNoLocno;
    
    private String statusStr;
    
    private String convertTypeStr;
    
    private String storeName;
    
    private String locnoName;

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
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

    public String getsQuality() {
        return sQuality;
    }

    public void setsQuality(String sQuality) {
        this.sQuality = sQuality;
    }

    public String getdQuality() {
        return dQuality;
    }

    public void setdQuality(String dQuality) {
        this.dQuality = dQuality;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getStoreNoLocno() {
		return storeNoLocno;
	}

	public void setStoreNoLocno(String storeNoLocno) {
		this.storeNoLocno = storeNoLocno;
	}

	public String getConvertTypeStr() {
		return convertTypeStr;
	}

	public void setConvertTypeStr(String convertTypeStr) {
		this.convertTypeStr = convertTypeStr;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLocnoName() {
		return locnoName;
	}

	public void setLocnoName(String locnoName) {
		this.locnoName = locnoName;
	}

	public String getdLocno() {
		return dLocno;
	}

	public void setdLocno(String dLocno) {
		this.dLocno = dLocno;
	}

	public String getUpdStatus() {
		return updStatus;
	}

	public void setUpdStatus(String updStatus) {
		this.updStatus = updStatus;
	}
	
}