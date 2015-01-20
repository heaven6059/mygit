package com.yougou.logistics.city.common.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-11 15:24:23
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
public class AccInventorySkuBookVo {
    /**
     * 序列号(流水号，由原序列SEQ_CON_CONTENT_MOVE生成)
     */
    private Long rowId;

    /**
     * 储位ID
     */
    private Long cellId;

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
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 容器编码
     */
    private String conNo;

    /**
     * 记帐方向(+1=借记 -1=贷记)
     */
    private Long direction;

    /**
     * 发生数量
     */
    private BigDecimal moveQty;
    
    
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
     * 结存数量
     */
    private BigDecimal balanceQty;

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
     * 财务类型(0=数量 1=预上 2=预下)
     */
    private String preFlag;

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
     * 包装数量(无用字段)
     */
    private BigDecimal packQty;
    

    /**
     * 手工移库标识(0=不允许手工移库 1=可手工移库)
     */
    private String hmManualFlag;
    
    /**
     * 是否需要更新容器商品明细账
     */
    private boolean needUpdateAccContainerSku=true;
    

    /**
     * 
     * {@linkplain #rowId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ROW_ID
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * 
     * {@linkplain #rowId}
     * @param rowId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ROW_ID
     */
    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    /**
     * 
     * {@linkplain #cellId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CELL_ID
     */
    public Long getCellId() {
        return cellId;
    }

    /**
     * 
     * {@linkplain #cellId}
     * @param cellId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CELL_ID
     */
    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CELL_NO
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     * @param cellNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CELL_NO
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ITEM_NO
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     * @param itemNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ITEM_NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 
     * {@linkplain #barcode}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BARCODE
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 
     * {@linkplain #barcode}
     * @param barcode the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BARCODE
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #conNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CON_NO
     */
    public String getConNo() {
        return conNo;
    }

    /**
     * 
     * {@linkplain #conNo}
     * @param conNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CON_NO
     */
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    /**
     * 
     * {@linkplain #direction}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.DIRECTION
     */
    public Long getDirection() {
        return direction;
    }

    /**
     * 
     * {@linkplain #direction}
     * @param direction the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.DIRECTION
     */
    public void setDirection(Long direction) {
        this.direction = direction;
    }

    /**
     * 
     * {@linkplain #moveQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.MOVE_QTY
     */
    public BigDecimal getMoveQty() {
        return moveQty;
    }

    /**
     * 
     * {@linkplain #moveQty}
     * @param moveQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.MOVE_QTY
     */
    public void setMoveQty(BigDecimal moveQty) {
        this.moveQty = moveQty;
    }

    /**
     * 
     * {@linkplain #balanceQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BALANCE_QTY
     */
    public BigDecimal getBalanceQty() {
        return balanceQty;
    }

    /**
     * 
     * {@linkplain #balanceQty}
     * @param balanceQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BALANCE_QTY
     */
    public void setBalanceQty(BigDecimal balanceQty) {
        this.balanceQty = balanceQty;
    }

    /**
     * 
     * {@linkplain #billNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BILL_NO
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 
     * {@linkplain #billNo}
     * @param billNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BILL_NO
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 
     * {@linkplain #billType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BILL_TYPE
     */
    public String getBillType() {
        return billType;
    }

    /**
     * 
     * {@linkplain #billType}
     * @param billType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.BILL_TYPE
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.IO_FLAG
     */
    public String getIoFlag() {
        return ioFlag;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     * @param ioFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.IO_FLAG
     */
    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #createdt}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATEDT
     */
    public Date getCreatedt() {
        return createdt;
    }

    /**
     * 
     * {@linkplain #createdt}
     * @param createdt the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.CREATEDT
     */
    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
    }

    /**
     * 
     * {@linkplain #preFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.PRE_FLAG
     */
    public String getPreFlag() {
        return preFlag;
    }

    /**
     * 
     * {@linkplain #preFlag}
     * @param preFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.PRE_FLAG
     */
    public void setPreFlag(String preFlag) {
        this.preFlag = preFlag;
    }

    /**
     * 
     * {@linkplain #seqId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.SEQ_ID
     */
    public BigDecimal getSeqId() {
        return seqId;
    }

    /**
     * 
     * {@linkplain #seqId}
     * @param seqId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.SEQ_ID
     */
    public void setSeqId(BigDecimal seqId) {
        this.seqId = seqId;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.TERMINAL_FLAG
     */
    public String getTerminalFlag() {
        return terminalFlag;
    }

    /**
     * 
     * {@linkplain #terminalFlag}
     * @param terminalFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.TERMINAL_FLAG
     */
    public void setTerminalFlag(String terminalFlag) {
        this.terminalFlag = terminalFlag;
    }

    /**
     * 
     * {@linkplain #statusTrans}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.STATUS_TRANS
     */
    public String getStatusTrans() {
        return statusTrans;
    }

    /**
     * 
     * {@linkplain #statusTrans}
     * @param statusTrans the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.STATUS_TRANS
     */
    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
    }

    /**
     * 
     * {@linkplain #packQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.PACK_QTY
     */
    public BigDecimal getPackQty() {
        return packQty;
    }

    /**
     * 
     * {@linkplain #packQty}
     * @param packQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU_BOOK.PACK_QTY
     */
    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getOutstockQty() {
		return outstockQty;
	}

	public void setOutstockQty(BigDecimal outstockQty) {
		this.outstockQty = outstockQty;
	}

	public BigDecimal getInstockQty() {
		return instockQty;
	}

	public void setInstockQty(BigDecimal instockQty) {
		this.instockQty = instockQty;
	}

	public String getHmManualFlag() {
		return hmManualFlag;
	}

	public void setHmManualFlag(String hmManualFlag) {
		this.hmManualFlag = hmManualFlag;
	}

	public boolean isNeedUpdateAccContainerSku() {
		return needUpdateAccContainerSku;
	}

	public void setNeedUpdateAccContainerSku(boolean needUpdateAccContainerSku) {
		this.needUpdateAccContainerSku = needUpdateAccContainerSku;
	}
    
}