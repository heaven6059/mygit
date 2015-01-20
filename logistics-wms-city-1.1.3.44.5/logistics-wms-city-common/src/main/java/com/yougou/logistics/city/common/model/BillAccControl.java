/*
 * 类名 com.yougou.logistics.city.common.model.BillAccControl
 * @author su.yq
 * @date  Mon Jan 13 19:48:58 CST 2014
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

import java.math.BigDecimal;

public class BillAccControl extends BillAccControlKey {
    private String billName;

    private BigDecimal immediateAcc;

    private BigDecimal dealFlag;

    private BigDecimal dealQty;

    private BigDecimal direction;

    private BigDecimal synAccIn;

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public BigDecimal getImmediateAcc() {
        return immediateAcc;
    }

    public void setImmediateAcc(BigDecimal immediateAcc) {
        this.immediateAcc = immediateAcc;
    }

    public BigDecimal getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(BigDecimal dealFlag) {
        this.dealFlag = dealFlag;
    }

    public BigDecimal getDealQty() {
        return dealQty;
    }

    public void setDealQty(BigDecimal dealQty) {
        this.dealQty = dealQty;
    }

    public BigDecimal getDirection() {
        return direction;
    }

    public void setDirection(BigDecimal direction) {
        this.direction = direction;
    }

    public BigDecimal getSynAccIn() {
        return synAccIn;
    }

    public void setSynAccIn(BigDecimal synAccIn) {
        this.synAccIn = synAccIn;
    }
}