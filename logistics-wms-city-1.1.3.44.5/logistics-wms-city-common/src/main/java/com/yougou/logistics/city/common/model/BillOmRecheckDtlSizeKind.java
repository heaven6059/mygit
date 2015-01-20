package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.dto.BaseSizeType;

/**
 * 
 * 分货复核单明细
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:18:20
 * @version 0.1.0
 * @copyright yougou.com
 */
public class BillOmRecheckDtlSizeKind extends BaseSizeType {

    private String locno;

    private String ownerNo;

    private String recheckNo;

    private String itemNo;

    private String sizeNo;

    private String sysNo;

    private String sizeKind;

    private String boxNo;

    private BigDecimal realQty;

    private BigDecimal allCount;

    public String getItemNo() {
	return itemNo;
    }

    public void setItemNo(String itemNo) {
	this.itemNo = itemNo;
    }

    public String getSizeNo() {
	return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
	this.sizeNo = sizeNo;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNo = sysNo;
    }

    public String getSizeKind() {
	return sizeKind;
    }

    public void setSizeKind(String sizeKind) {
	this.sizeKind = sizeKind;
    }

    public BigDecimal getRealQty() {
	return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
	this.realQty = realQty;
    }

    public BigDecimal getAllCount() {
	return allCount;
    }

    public void setAllCount(BigDecimal allCount) {
	this.allCount = allCount;
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

    public String getRecheckNo() {
	return recheckNo;
    }

    public void setRecheckNo(String recheckNo) {
	this.recheckNo = recheckNo;
    }

    public String getBoxNo() {
	return boxNo;
    }

    public void setBoxNo(String boxNo) {
	this.boxNo = boxNo;
    }

}