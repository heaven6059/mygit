package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-06-18 10:38:59
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
public class ConContentHistoryKey {
    private String locno;

    private String cellNo;

    private Long cellId;

    private Date contentDate;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public Long getCellId() {
        return cellId;
    }

    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    public Date getContentDate() {
        return contentDate;
    }

    public void setContentDate(Date contentDate) {
        this.contentDate = contentDate;
    }
}