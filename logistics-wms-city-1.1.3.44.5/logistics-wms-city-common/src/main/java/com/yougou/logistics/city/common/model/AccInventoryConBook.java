package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public class AccInventoryConBook {
    /**
     * 序列号(流水号，由序列SEQ_ACC_INVENTORY_CON_BOOK生成)
     */
    private Long rowId;

    /**
     * 容器编码
     */
    private String conNo;

    /**
     * 仓别
     */
    private String locno;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 储位编码
     */
    private String cellNo;

    /**
     * 商品属性类型
     */
    private String itemType;

    /**
     * 品质
     */
    private String quality;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 记帐方向(+1=借记 -1=贷记)
     */
    private Long direction;

    /**
     * 发生子容器数
     */
    private BigDecimal moveChildrenQty;

    /**
     * 结存子容器数
     */
    private BigDecimal balanceChildrenQty;

    /**
     * 发生SKU数量
     */
    private BigDecimal moveSkuQty;

    /**
     * 结存SKU数量
     */
    private BigDecimal balanceSkuQty;

    /**
     * 单据编码
     */
    private String billNo;

    /**
     * 单据类型
     */
    private String billType;

    /**
     * 进出标识(I=入库 O=出库)
     */
    private String ioFlag;

    /**
     * 记帐人员
     */
    private String creator;

    /**
     * 记帐时间
     */
    private Date createtm;

    /**
     * 记帐日期
     */
    private Date createdt;

    /**
     * 顺序号(对应单据的顺序号)
     */
    private BigDecimal seqId;

    /**
     * 操作终端标识(1=前台 2=RF 3=DPS 4=AS)
     */
    private String terminalFlag;

    /**
     * 传输标识(10=未传输 13=已传输)
     */
    private String statusTrans;

    /**
     * 
     * {@linkplain #rowId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.ROW_ID
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * 
     * {@linkplain #rowId}
     * @param rowId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.ROW_ID
     */
    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    /**
     * 
     * {@linkplain #conNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CON_NO
     */
    public String getConNo() {
        return conNo;
    }

    /**
     * 
     * {@linkplain #conNo}
     * @param conNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CON_NO
     */
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CELL_NO
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     * @param cellNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CELL_NO
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #direction}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.DIRECTION
     */
    public Long getDirection() {
        return direction;
    }

    /**
     * 
     * {@linkplain #direction}
     * @param direction the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.DIRECTION
     */
    public void setDirection(Long direction) {
        this.direction = direction;
    }

    /**
     * 
     * {@linkplain #moveChildrenQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.MOVE_CHILDREN_QTY
     */
    public BigDecimal getMoveChildrenQty() {
        return moveChildrenQty;
    }

    /**
     * 
     * {@linkplain #moveChildrenQty}
     * @param moveChildrenQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.MOVE_CHILDREN_QTY
     */
    public void setMoveChildrenQty(BigDecimal moveChildrenQty) {
        this.moveChildrenQty = moveChildrenQty;
    }

    /**
     * 
     * {@linkplain #balanceChildrenQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BALANCE_CHILDREN_QTY
     */
    public BigDecimal getBalanceChildrenQty() {
        return balanceChildrenQty;
    }

    /**
     * 
     * {@linkplain #balanceChildrenQty}
     * @param balanceChildrenQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BALANCE_CHILDREN_QTY
     */
    public void setBalanceChildrenQty(BigDecimal balanceChildrenQty) {
        this.balanceChildrenQty = balanceChildrenQty;
    }

    /**
     * 
     * {@linkplain #moveSkuQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.MOVE_SKU_QTY
     */
    public BigDecimal getMoveSkuQty() {
        return moveSkuQty;
    }

    /**
     * 
     * {@linkplain #moveSkuQty}
     * @param moveSkuQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.MOVE_SKU_QTY
     */
    public void setMoveSkuQty(BigDecimal moveSkuQty) {
        this.moveSkuQty = moveSkuQty;
    }

    /**
     * 
     * {@linkplain #balanceSkuQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BALANCE_SKU_QTY
     */
    public BigDecimal getBalanceSkuQty() {
        return balanceSkuQty;
    }

    /**
     * 
     * {@linkplain #balanceSkuQty}
     * @param balanceSkuQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BALANCE_SKU_QTY
     */
    public void setBalanceSkuQty(BigDecimal balanceSkuQty) {
        this.balanceSkuQty = balanceSkuQty;
    }

    /**
     * 
     * {@linkplain #billNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BILL_NO
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 
     * {@linkplain #billNo}
     * @param billNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BILL_NO
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 
     * {@linkplain #billType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BILL_TYPE
     */
    public String getBillType() {
        return billType;
    }

    /**
     * 
     * {@linkplain #billType}
     * @param billType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.BILL_TYPE
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.IO_FLAG
     */
    public String getIoFlag() {
        return ioFlag;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     * @param ioFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.IO_FLAG
     */
    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #createdt}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATEDT
     */
    public Date getCreatedt() {
        return createdt;
    }

    /**
     * 
     * {@linkplain #createdt}
     * @param createdt the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.CREATEDT
     */
    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
    }

    /**
     * 
     * {@linkplain #seqId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.SEQ_ID
     */
    public BigDecimal getSeqId() {
        return seqId;
    }

    /**
     * 
     * {@linkplain #seqId}
     * @param seqId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.SEQ_ID
     */
    public void setSeqId(BigDecimal seqId) {
        this.seqId = seqId;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.TERMINAL_FLAG
     */
    public String getTerminalFlag() {
        return terminalFlag;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     * @param terminalFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.TERMINAL_FLAG
     */
    public void setTerminalFlag(String terminalFlag) {
        this.terminalFlag = terminalFlag;
    }

    /**
     * 
     * {@linkplain #statusTrans}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.STATUS_TRANS
     */
    public String getStatusTrans() {
        return statusTrans;
    }

    /**
     * 
     * {@linkplain #statusTrans}
     * @param statusTrans the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON_BOOK.STATUS_TRANS
     */
    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
    }
}