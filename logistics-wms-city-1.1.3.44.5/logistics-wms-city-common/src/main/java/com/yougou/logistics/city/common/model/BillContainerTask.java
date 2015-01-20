package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
public class BillContainerTask extends BillContainerTaskKey {
    /**
     * 状态 10：建单,13：审核
     */
    private String status;

    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建人名称
     */
    private String creatorname;

    /**
     * 创建时间
     */
    private Date createtm;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 修改人名称
     */
    private String editorname;
    
    /**
     * 修改时间
     */
    private Date edittm;

    /**
     * 作业类型  A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
     */
    private String useType;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核人名称
     */
    private String auditorname;
    
    /**
     * 审核时间
     */
    private Date audittm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 业务类型,0:基础容器,1:业务单据容器
     */
    private String businessType;

    /**
     * 
     * {@linkplain #status}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * {@linkplain #status}
     * @param status the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.STATUS
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #editor}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.EDITOR
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 
     * {@linkplain #editor}
     * @param editor the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.EDITOR
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 
     * {@linkplain #edittm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.EDITTM
     */
    public Date getEdittm() {
        return edittm;
    }

    /**
     * 
     * {@linkplain #edittm}
     * @param edittm the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.EDITTM
     */
    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    /**
     * 
     * {@linkplain #useType}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.USE_TYPE
     */
    public String getUseType() {
        return useType;
    }

    /**
     * 
     * {@linkplain #useType}
     * @param useType the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.USE_TYPE
     */
    public void setUseType(String useType) {
        this.useType = useType;
    }

    /**
     * 
     * {@linkplain #auditor}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.AUDITOR
     */
    public String getAuditor() {
        return auditor;
    }

    /**
     * 
     * {@linkplain #auditor}
     * @param auditor the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.AUDITOR
     */
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    /**
     * 
     * {@linkplain #audittm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.AUDITTM
     */
    public Date getAudittm() {
        return audittm;
    }

    /**
     * 
     * {@linkplain #audittm}
     * @param audittm the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.AUDITTM
     */
    public void setAudittm(Date audittm) {
        this.audittm = audittm;
    }

    /**
     * 
     * {@linkplain #remark}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * {@linkplain #remark}
     * @param remark the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.REMARK
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 
     * {@linkplain #businessType}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.BUSINESS_TYPE
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 
     * {@linkplain #businessType}
     * @param businessType the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK.BUSINESS_TYPE
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

	public String getAuditorname() {
		return auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}
    
    
}