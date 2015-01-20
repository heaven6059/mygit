package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
public class BillOmExpKey {
    private String locno;

    private String expNo;

    private String expType;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }
}