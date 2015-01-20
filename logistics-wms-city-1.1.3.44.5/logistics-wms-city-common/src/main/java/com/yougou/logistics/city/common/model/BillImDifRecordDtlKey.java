package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
public class BillImDifRecordDtlKey {
    private String locno;

    private String ownerNo;

    private String defRecordNo;

    private Short poId;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getDefRecordNo() {
        return defRecordNo;
    }

    public void setDefRecordNo(String defRecordNo) {
        this.defRecordNo = defRecordNo;
    }

    public Short getPoId() {
        return poId;
    }

    public void setPoId(Short poId) {
        this.poId = poId;
    }
}