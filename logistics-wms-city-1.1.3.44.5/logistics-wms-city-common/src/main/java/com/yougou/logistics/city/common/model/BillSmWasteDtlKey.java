package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
public class BillSmWasteDtlKey {
    private String wasteNo;

    private String locno;

    private String ownerNo;

    private Short rowId;

    public String getWasteNo() {
        return wasteNo;
    }

    public void setWasteNo(String wasteNo) {
        this.wasteNo = wasteNo;
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

    public Short getRowId() {
        return rowId;
    }

    public void setRowId(Short rowId) {
        this.rowId = rowId;
    }
}