/*
 * 类名 com.yougou.logistics.city.common.model.BillOmLocateDtlKey
 * @author su.yq
 * @date  Mon Nov 04 14:35:57 CST 2013
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

public class BillOmLocateDtlKey {
    private String locateNo;

    private String locno;

    private String ownerNo;

    private Long rowId;

    public String getLocateNo() {
        return locateNo;
    }

    public void setLocateNo(String locateNo) {
        this.locateNo = locateNo;
    }

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

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}