/*
 * 类名 com.yougou.logistics.city.common.model.BillImCheckPalKey
 * @author qin.dy
 * @date  Sat Nov 09 14:56:56 CST 2013
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

public class BillImCheckPalKey {
    private String locno;

    private String ownerNo;

    private String sCheckNo;

    private String checkNo;

    private Short checkRowId;

    private String containerNo;

    private String labelNo;

    private String scanLabelNo;

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

    public String getsCheckNo() {
        return sCheckNo;
    }

    public void setsCheckNo(String sCheckNo) {
        this.sCheckNo = sCheckNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public Short getCheckRowId() {
        return checkRowId;
    }

    public void setCheckRowId(Short checkRowId) {
        this.checkRowId = checkRowId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getScanLabelNo() {
        return scanLabelNo;
    }

    public void setScanLabelNo(String scanLabelNo) {
        this.scanLabelNo = scanLabelNo;
    }
}