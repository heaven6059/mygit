/*
 * 类名 com.yougou.logistics.city.common.model.BillWmOutstockDirectKey
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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

import java.util.Date;


public class BillWmOutstockDirectKey {
	
    private Date operateDate;

    private Integer directSerial;

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Integer getDirectSerial() {
        return directSerial;
    }

    public void setDirectSerial(Integer directSerial) {
        this.directSerial = directSerial;
    }
}