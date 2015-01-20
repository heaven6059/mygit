package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
public class BillOmDivideDifferentDtlKey {
    private String locno;

    private String differentNo;

    private BigDecimal serialNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getDifferentNo() {
		return differentNo;
	}

	public void setDifferentNo(String differentNo) {
		this.differentNo = differentNo;
	}

	public BigDecimal getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(BigDecimal serialNo) {
		this.serialNo = serialNo;
	}
}