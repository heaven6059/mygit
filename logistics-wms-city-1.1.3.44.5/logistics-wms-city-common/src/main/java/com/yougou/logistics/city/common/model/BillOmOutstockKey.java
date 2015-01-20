/*
 * 类名 com.yougou.logistics.city.common.model.BillOmOutstockKey
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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

public class BillOmOutstockKey {
    private String locno;

    private String outstockNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getOutstockNo() {
        return outstockNo;
    }

    public void setOutstockNo(String outstockNo) {
        this.outstockNo = outstockNo;
    }
}