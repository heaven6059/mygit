/*
 * 类名 com.yougou.logistics.city.common.model.CsInstockSetting
 * @author luo.hl
 * @date  Tue Oct 08 15:13:38 CST 2013
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

public class CsInstockSetting extends CsInstockSettingKey {
    private String ownerNo;

    private String setType;

    private String settingName;

    private String settingDesc;

    private String status;

    private String memo;

    private String instockScope;

    private String cellSort;

    private String limitedType;

    private String sameQualityFlag;

    private String sameItemFlag;

    private String emptyCellFlag;

    private String detailType;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private Date starttm;

    private Date stoptm;
    
    private String creatorName;
    
    private String editorName;

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

	public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingDesc() {
        return settingDesc;
    }

    public void setSettingDesc(String settingDesc) {
        this.settingDesc = settingDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getInstockScope() {
        return instockScope;
    }

    public void setInstockScope(String instockScope) {
        this.instockScope = instockScope;
    }

    public String getCellSort() {
        return cellSort;
    }

    public void setCellSort(String cellSort) {
        this.cellSort = cellSort;
    }

    public String getLimitedType() {
        return limitedType;
    }

    public void setLimitedType(String limitedType) {
        this.limitedType = limitedType;
    }

    public String getSameQualityFlag() {
        return sameQualityFlag;
    }

    public void setSameQualityFlag(String sameQualityFlag) {
        this.sameQualityFlag = sameQualityFlag;
    }

    public String getSameItemFlag() {
        return sameItemFlag;
    }

    public void setSameItemFlag(String sameItemFlag) {
        this.sameItemFlag = sameItemFlag;
    }

    public String getEmptyCellFlag() {
        return emptyCellFlag;
    }

    public void setEmptyCellFlag(String emptyCellFlag) {
        this.emptyCellFlag = emptyCellFlag;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
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

    public Date getStarttm() {
        return starttm;
    }

    public void setStarttm(Date starttm) {
        this.starttm = starttm;
    }

    public Date getStoptm() {
        return stoptm;
    }

    public void setStoptm(Date stoptm) {
        this.stoptm = stoptm;
    }
}