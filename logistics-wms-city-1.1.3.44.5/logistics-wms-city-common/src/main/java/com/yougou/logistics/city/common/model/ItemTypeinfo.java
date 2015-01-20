package com.yougou.logistics.city.common.model;

import java.util.Date;

public class ItemTypeinfo extends ItemTypeinfoKey {
    private String itemvalue;

    private String itemname;

    private Date edittm;

    private String editor;

    private Date createtm;

    private String creator;

    private String remarks;

    private Date recievetm;
    
    private String lookupcodeStr;

    public String getItemvalue() {
        return itemvalue;
    }

    public void setItemvalue(String itemvalue) {
        this.itemvalue = itemvalue;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
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

    public Date getCreatetm() {
        return createtm;
    }

    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getRecievetm() {
        return recievetm;
    }

    public void setRecievetm(Date recievetm) {
        this.recievetm = recievetm;
    }

	public String getLookupcodeStr() {
		return lookupcodeStr;
	}

	public void setLookupcodeStr(String lookupcodeStr) {
		this.lookupcodeStr = lookupcodeStr;
	}
}