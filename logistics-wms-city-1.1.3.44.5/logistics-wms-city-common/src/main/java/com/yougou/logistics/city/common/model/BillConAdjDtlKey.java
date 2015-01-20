package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public class BillConAdjDtlKey {
    private String adjNo;

    private Short rowId;

    private String locno;

    public String getAdjNo() {
        return adjNo;
    }

    public void setAdjNo(String adjNo) {
        this.adjNo = adjNo;
    }

    public Short getRowId() {
        return rowId;
    }

    public void setRowId(Short rowId) {
        this.rowId = rowId;
    }

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }
}