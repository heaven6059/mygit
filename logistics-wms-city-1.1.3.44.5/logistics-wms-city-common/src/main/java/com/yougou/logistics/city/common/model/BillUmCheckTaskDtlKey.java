/*
 * 类名 com.yougou.logistics.city.common.model.BillUmCheckTaskDtlKey
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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

public class BillUmCheckTaskDtlKey {
    private String locno;

    private String ownerNo;

    private String checkTaskNo;

    private Integer rowId;

    private String untreadNo;

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

    public String getCheckTaskNo() {
        return checkTaskNo;
    }

    public void setCheckTaskNo(String checkTaskNo) {
        this.checkTaskNo = checkTaskNo;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getUntreadNo() {
        return untreadNo;
    }

    public void setUntreadNo(String untreadNo) {
        this.untreadNo = untreadNo;
    }
}