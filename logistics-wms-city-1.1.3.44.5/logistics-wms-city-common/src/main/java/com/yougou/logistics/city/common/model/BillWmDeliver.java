package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
public class BillWmDeliver extends BillWmDeliverKey {
	
	@JsonSerialize(using =JsonDateSerializer$10.class)
    private Date operateDate;

    private String status;
    //页面展示的单据类型
    private String showStatus;
    private String creator;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String sendFlag;

    private BigDecimal totalVolumn;

    private String sysNo;

    private String auditor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date audittm;

    private String supplierNo;

    private String memo;
    
    private String brandNo;

    //创建人中文名称
    private String creatorName;
    //审核人中文名称
    private String auditorName;
    
    private String editorName;
    public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.showStatus = SystemCache.getLookUpName("CITY_DELIVER_STATUS", status);
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

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public BigDecimal getTotalVolumn() {
        return totalVolumn;
    }

    public void setTotalVolumn(BigDecimal totalVolumn) {
        this.totalVolumn = totalVolumn;
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

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}
	
}