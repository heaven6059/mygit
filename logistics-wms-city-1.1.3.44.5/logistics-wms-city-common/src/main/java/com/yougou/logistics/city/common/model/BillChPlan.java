/*
 * 类名 com.yougou.logistics.city.common.model.BillChPlan
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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

public class BillChPlan extends BillChPlanKey {
    private String ownerNo;

    private String limitBrandFlag;
    
    private String brandNo;
    
    private String planType;

    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date planDate;
    
    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date beginDate;

    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date endDate;

    private String status;
    
    private String sourceStatus;

    private String createFlag;

    private String planRemark;

    private String creator;
    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;
    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date edittm;

    private String sourcePlanNo;

    private BigDecimal sendFlag;

    private String sysNo;

    private String auditor;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date audittm;

    private String quality;
    
    private String itemType;
    
    private String creatorName;//创建人中文名称
    private String editorName;//修改人中文名称
    private String auditorName;//审核人中文名称
    
    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getLimitBrandFlag() {
		return limitBrandFlag;
	}

	public void setLimitBrandFlag(String limitBrandFlag) {
		this.limitBrandFlag = limitBrandFlag;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
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

    public String getSourcePlanNo() {
        return sourcePlanNo;
    }

    public void setSourcePlanNo(String sourcePlanNo) {
        this.sourcePlanNo = sourcePlanNo;
    }

    public BigDecimal getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(BigDecimal sendFlag) {
        this.sendFlag = sendFlag;
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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