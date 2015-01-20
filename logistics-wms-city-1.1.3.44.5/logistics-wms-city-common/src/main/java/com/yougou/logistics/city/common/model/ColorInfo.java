package com.yougou.logistics.city.common.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;


public class ColorInfo {
    private String colorNo;

    private String colorCode;

    private String colorName;

    private String sysNo;

    private String creator;

    /*新增关联字段*/
    private String sysNoStr;  
    /*END*/
    
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String remarks;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String editor;
    
    private String recievetm;
    
    /**新增接受时间字段**/
    public String getRecievetm() {
        return recievetm;
    }

    public void setRecievetm(String recievetm) {
        this.recievetm = recievetm;
    }

    public String getColorNo() {
        return colorNo;
    }

    public void setColorNo(String colorNo) {
        this.colorNo = colorNo;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
    
    /*新增关联字段*/
	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}
    /*END*/
}