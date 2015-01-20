/*
 * 类名 com.yougou.logistics.city.common.model.BillWmPlanKey
 * @author yougoupublic
 * @date  Fri Mar 21 14:04:26 CST 2014
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

public class BillWmPlanKey {
    private String locno;

    private String ownerNo;

    private String planNo;

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

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }
}