package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
public class BillWmDeliverDtlKey {
    private String deliverNo;

    private String locno;

    private String ownerNo;

    private String recedeNo;

    private Short poId;

    private String itemNo;

    private Long itemId;

    private String cellNo;

    private Long cellId;

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
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

    public String getRecedeNo() {
        return recedeNo;
    }

    public void setRecedeNo(String recedeNo) {
        this.recedeNo = recedeNo;
    }

    public Short getPoId() {
        return poId;
    }

    public void setPoId(Short poId) {
        this.poId = poId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
}