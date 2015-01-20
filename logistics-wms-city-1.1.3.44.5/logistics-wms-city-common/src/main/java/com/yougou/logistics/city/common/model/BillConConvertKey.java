/*
 * 类名 com.yougou.logistics.city.common.model.BillConConvertKey
 * @author su.yq
 * @date  Thu Jun 05 10:47:51 CST 2014
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

public class BillConConvertKey {
    private String locno;

    private String ownerNo;

    private String convertNo;

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

    public String getConvertNo() {
        return convertNo;
    }

    public void setConvertNo(String convertNo) {
        this.convertNo = convertNo;
    }
}