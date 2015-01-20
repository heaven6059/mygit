package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;


@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class SizeInfo extends SizeInfoKey {
    private String sizeCode;

    private String sizeName;

    private String sysNo;

    private BigDecimal hcolNo;

    private String workSize;

    private String creator;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String remarks;
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String editor;
    
    private String sizeKindStr;
    
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date recievetm;

    public Date getRecievetm() {
        return recievetm;
    }

    public void setRecievetm(Date recievetm) {
        this.recievetm = recievetm;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public BigDecimal getHcolNo() {
        return hcolNo;
    }

    public void setHcolNo(BigDecimal hcolNo) {
        this.hcolNo = hcolNo;
    }

    public String getWorkSize() {
        return workSize;
    }

    public void setWorkSize(String workSize) {
        this.workSize = workSize;
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

	public String getSizeKindStr() {
		return sizeKindStr;
	}

	public void setSizeKindStr(String sizeKindStr) {
		this.sizeKindStr = sizeKindStr;
	}
    
}