package com.yougou.logistics.city.common.model;

import java.util.Date;

import com.yougou.logistics.city.common.enums.BmdeflocStatusEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class BmDefloc {
    private String locno;

    private String locname;

    private String memo;

    private String createFlag;
    
    private String  createFlagStr;

    private String creator;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String locType;
    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
        this.createFlagStr = BmdeflocStatusEnums.getTextByStatus(createFlag);
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

	public String getCreateFlagStr() {
		return createFlagStr;
	}

	public void setCreateFlagStr(String createFlagStr) {
		this.createFlagStr = createFlagStr;
	}

	public String getLocType() {
		return locType;
	}

	public void setLocType(String locType) {
		this.locType = locType;
	}
	
}