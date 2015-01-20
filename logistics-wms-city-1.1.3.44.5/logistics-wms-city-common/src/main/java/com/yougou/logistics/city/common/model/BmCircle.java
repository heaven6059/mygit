package com.yougou.logistics.city.common.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.enums.BmCircleEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BmCircle extends BmCircleKey {
    private String circleName;

    private String memo;

    private String createFlag;
    
    private String createFlagStr;

    private String creator;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
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
        this.createFlagStr = BmCircleEnums.getTextByStatus(createFlag);
    }

    public String getCreateFlagStr() {
		return createFlagStr;
	}

	public void setCreateFlagStr(String createFlagStr) {
		this.createFlagStr = createFlagStr;
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
}