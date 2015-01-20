/*
 * 类名 com.yougou.logistics.city.common.model.BillConStorelockKey
 * @author yougoupublic
 * @date  Sat Mar 08 11:25:53 CST 2014
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

public class BillConStorelockKey {
    private String locno;

    private String ownerNo;

    private String storelockNo;

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

    public String getStorelockNo() {
        return storelockNo;
    }

    public void setStorelockNo(String storelockNo) {
        this.storelockNo = storelockNo;
    }
}