package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class Lookup extends LookupKey {
    private String lookupname;

    private BigDecimal lookuplevel;

    private BigDecimal auditstatus;

    private String remarks;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date edittm;

    private String brandNo;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date createtm;

    private String creator;

    private String editor;
  
    public String getLookupname() {
        return lookupname;
    }

    public void setLookupname(String lookupname) {
        this.lookupname = lookupname;
    }

    public BigDecimal getLookuplevel() {
        return lookuplevel;
    }

    public void setLookuplevel(BigDecimal lookuplevel) {
        this.lookuplevel = lookuplevel;
    }

    public BigDecimal getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(BigDecimal auditstatus) {
        this.auditstatus = auditstatus;
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

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
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

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}