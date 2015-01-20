package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-24 13:44:52
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
public class AccTaskDtl extends AccTaskDtlKey {
    /**
     * 储位ID
     */
    private Long cellId;

    /**
     * 仓别
     */
    private String locno;

    /**
     * 储位编码
     */
    private String cellNo;

    /**
     * 商品编码
     */
    private String itemNo;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 商品属性类型
     */
    private String itemType;

    /**
     * 品质
     */
    private String quality;

    /**
     * 委托业主编码
     */
    private String ownerNo;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 箱号
     */
    private String boxNo;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 预下数量
     */
    private BigDecimal outstockQty;

    /**
     * 预上数量
     */
    private BigDecimal instockQty;

    /**
     * 盘点时用于锁定(0：可用；1：锁定)
     */
    private String status;

    /**
     * 库存冻结标识(0-可用；1-冻结)
     */
    private String flag;

    /**
     * 手工移库标识(0:不允许手工移库；1：可手工移库)
     */
    private String hmManualFlag;

    /**
     * 记帐人员
     */
    private String creator;

    /**
     * 操作终端标识(1：前台；2：RF；3：DPS；4：AS)
     */
    private String terminalFlag;

    /**
     * 
     * {@linkplain #cellId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.CELL_ID
     */
    public Long getCellId() {
        return cellId;
    }

    /**
     * 
     * {@linkplain #cellId}
     * @param cellId the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.CELL_ID
     */
    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #cellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.CELL_NO
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     * @param cellNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.CELL_NO
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.ITEM_NO
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     * @param itemNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.ITEM_NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 
     * {@linkplain #barcode}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.BARCODE
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 
     * {@linkplain #barcode}
     * @param barcode the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.BARCODE
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #boxNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.BOX_NO
     */
    public String getBoxNo() {
        return boxNo;
    }

    /**
     * 
     * {@linkplain #boxNo}
     * @param boxNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.BOX_NO
     */
    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    /**
     * 
     * {@linkplain #qty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.QTY
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * 
     * {@linkplain #qty}
     * @param qty the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.QTY
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * 
     * {@linkplain #outstockQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.OUTSTOCK_QTY
     */
    public BigDecimal getOutstockQty() {
        return outstockQty;
    }

    /**
     * 
     * {@linkplain #outstockQty}
     * @param outstockQty the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.OUTSTOCK_QTY
     */
    public void setOutstockQty(BigDecimal outstockQty) {
        this.outstockQty = outstockQty;
    }

    /**
     * 
     * {@linkplain #instockQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.INSTOCK_QTY
     */
    public BigDecimal getInstockQty() {
        return instockQty;
    }

    /**
     * 
     * {@linkplain #instockQty}
     * @param instockQty the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.INSTOCK_QTY
     */
    public void setInstockQty(BigDecimal instockQty) {
        this.instockQty = instockQty;
    }

    /**
     * 
     * {@linkplain #status}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * {@linkplain #status}
     * @param status the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.STATUS
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * {@linkplain #flag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.FLAG
     */
    public String getFlag() {
        return flag;
    }

    /**
     * 
     * {@linkplain #flag}
     * @param flag the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.FLAG
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * 
     * {@linkplain #hmManualFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.HM_MANUAL_FLAG
     */
    public String getHmManualFlag() {
        return hmManualFlag;
    }

    /**
     * 
     * {@linkplain #hmManualFlag}
     * @param hmManualFlag the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.HM_MANUAL_FLAG
     */
    public void setHmManualFlag(String hmManualFlag) {
        this.hmManualFlag = hmManualFlag;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.TERMINAL_FLAG
     */
    public String getTerminalFlag() {
        return terminalFlag;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     * @param terminalFlag the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.TERMINAL_FLAG
     */
    public void setTerminalFlag(String terminalFlag) {
        this.terminalFlag = terminalFlag;
    }
}