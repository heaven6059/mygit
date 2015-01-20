/*
 * 类名 com.yougou.logistics.city.common.model.BillUmUntreadMmKey
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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

public class BillUmUntreadMmKey {
    private String locno;

    private String ownerNo;

    private String untreadMmNo;

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

    public String getUntreadMmNo() {
        return untreadMmNo;
    }

    public void setUntreadMmNo(String untreadMmNo) {
        this.untreadMmNo = untreadMmNo;
    }
}