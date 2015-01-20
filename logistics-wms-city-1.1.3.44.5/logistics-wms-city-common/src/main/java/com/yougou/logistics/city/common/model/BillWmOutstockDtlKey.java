/*
 * 类名 com.yougou.logistics.city.common.model.BillWmOutstockDtlKey
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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

public class BillWmOutstockDtlKey {
    private String outstockNo;

    private String locno;

    private String ownerNo;

    private Integer divideId;

    public String getOutstockNo() {
        return outstockNo;
    }

    public void setOutstockNo(String outstockNo) {
        this.outstockNo = outstockNo;
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

    public Integer getDivideId() {
        return divideId;
    }

    public void setDivideId(Integer divideId) {
        this.divideId = divideId;
    }
}