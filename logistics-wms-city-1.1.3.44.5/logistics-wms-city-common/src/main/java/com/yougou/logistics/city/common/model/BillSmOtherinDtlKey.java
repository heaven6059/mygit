/*
 * 类名 com.yougou.logistics.city.common.model.BillSmOtherinDtlKey
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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

public class BillSmOtherinDtlKey {
    private String locno;

    private String ownerNo;

    private String otherinNo;
    
    private Short rowId;

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

    public String getOtherinNo() {
        return otherinNo;
    }

    public void setOtherinNo(String otherinNo) {
        this.otherinNo = otherinNo;
    }

	public Short getRowId() {
		return rowId;
	}

	public void setRowId(Short rowId) {
		this.rowId = rowId;
	}
}