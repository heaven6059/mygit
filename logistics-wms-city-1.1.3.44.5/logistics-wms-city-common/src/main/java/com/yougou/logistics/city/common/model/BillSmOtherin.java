/*
 * 类名 com.yougou.logistics.city.common.model.BillSmOtherin
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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

public class BillSmOtherin extends BillSmOtherinKey {
    private String otherinType;

    @JsonSerialize(using = JsonDateSerializer$10.class)
    private Date instorageDate;

    private String status;
    
    private String updStatus;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String auditor;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date audittm;

    private String operator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date operatetm;

    private String remark;
    
	private String areaQuality;

	private String itemType;

	private String poNo;
	
	private BigDecimal instorageQty;
	//对应的中文名称
	private String creatorName;
	private String editorName;
	private String auditorName;
	private String operatorName;
    public String getOtherinType() {
        return otherinType;
    }

    public void setOtherinType(String otherinType) {
        this.otherinType = otherinType;
    }

    public Date getInstorageDate() {
        return instorageDate;
    }

    public void setInstorageDate(Date instorageDate) {
        this.instorageDate = instorageDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperatetm() {
        return operatetm;
    }

    public void setOperatetm(Date operatetm) {
        this.operatetm = operatetm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getAreaQuality() {
		return areaQuality;
	}

	public void setAreaQuality(String areaQuality) {
		this.areaQuality = areaQuality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public BigDecimal getInstorageQty() {
		return instorageQty;
	}

	public void setInstorageQty(BigDecimal instorageQty) {
		this.instorageQty = instorageQty;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
}