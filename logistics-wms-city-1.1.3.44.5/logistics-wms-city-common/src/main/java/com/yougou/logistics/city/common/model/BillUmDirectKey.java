package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2014-01-15 14:36:28
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
public class BillUmDirectKey {
    private String locno;

    private String untreadMmNo;

    private Integer rowId;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getUntreadMmNo() {
        return untreadMmNo;
    }

    public void setUntreadMmNo(String untreadMmNo) {
        this.untreadMmNo = untreadMmNo;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }
}