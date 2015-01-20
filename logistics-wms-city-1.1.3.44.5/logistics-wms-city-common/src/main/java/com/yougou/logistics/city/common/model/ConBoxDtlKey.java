package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
public class ConBoxDtlKey {
    private String locno;

    private String boxNo;

    private BigDecimal boxId;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public BigDecimal getBoxId() {
        return boxId;
    }

    public void setBoxId(BigDecimal boxId) {
        this.boxId = boxId;
    }
}