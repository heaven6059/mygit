package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2014-01-17 22:26:59
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
public class BillUmInstockKey {
    private String locno;

    private String ownerNo;

    private String instockNo;

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

    public String getInstockNo() {
        return instockNo;
    }

    public void setInstockNo(String instockNo) {
        this.instockNo = instockNo;
    }
}