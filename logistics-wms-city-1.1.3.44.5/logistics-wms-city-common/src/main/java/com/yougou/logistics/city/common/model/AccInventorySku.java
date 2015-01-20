package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

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
public class AccInventorySku {
    /**
     * 储位ID(流水号，由原序列SEQ_CON_CONTENT生成)
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
    private String itemName;

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
     * 包装数量(暂不使用)
     */
    private BigDecimal packQty;

    /**
     * 例外数量(暂不使用)
     */
    private BigDecimal unusualQty;

    /**
     * 盘点锁定标识(0=可用 1=锁定)
     */
    private String status;

    /**
     * 库存冻结标识(0=可用 1=冻结)
     */
    private String flag;

    /**
     * 手工移库标识(0=不允许手工移库 1=可手工移库)
     */
    private String hmManualFlag;

    /**
     * 记帐人员
     */
    private String creator;

    /**
     * 记帐时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    /**
     * 修改人员
     */
    private String editor;

    /**
     * 修改时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    /**
     * 
     * {@linkplain #cellId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CELL_ID
     */
    public Long getCellId() {
        return cellId;
    }

    /**
     * 
     * {@linkplain #cellId}
     * @param cellId the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CELL_ID
     */
    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CELL_NO
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     * @param cellNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CELL_NO
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.ITEM_NO
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     * @param itemNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.ITEM_NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 
     * {@linkplain #barcode}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.BARCODE
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 
     * {@linkplain #barcode}
     * @param barcode the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.BARCODE
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #qty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.QTY
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * 
     * {@linkplain #qty}
     * @param qty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.QTY
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * 
     * {@linkplain #outstockQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.OUTSTOCK_QTY
     */
    public BigDecimal getOutstockQty() {
        return outstockQty;
    }

    /**
     * 
     * {@linkplain #outstockQty}
     * @param outstockQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.OUTSTOCK_QTY
     */
    public void setOutstockQty(BigDecimal outstockQty) {
        this.outstockQty = outstockQty;
    }

    /**
     * 
     * {@linkplain #instockQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.INSTOCK_QTY
     */
    public BigDecimal getInstockQty() {
        return instockQty;
    }

    /**
     * 
     * {@linkplain #instockQty}
     * @param instockQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.INSTOCK_QTY
     */
    public void setInstockQty(BigDecimal instockQty) {
        this.instockQty = instockQty;
    }

    /**
     * 
     * {@linkplain #packQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.PACK_QTY
     */
    public BigDecimal getPackQty() {
        return packQty;
    }

    /**
     * 
     * {@linkplain #packQty}
     * @param packQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.PACK_QTY
     */
    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    /**
     * 
     * {@linkplain #unusualQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.UNUSUAL_QTY
     */
    public BigDecimal getUnusualQty() {
        return unusualQty;
    }

    /**
     * 
     * {@linkplain #unusualQty}
     * @param unusualQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.UNUSUAL_QTY
     */
    public void setUnusualQty(BigDecimal unusualQty) {
        this.unusualQty = unusualQty;
    }

    /**
     * 
     * {@linkplain #status}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * {@linkplain #status}
     * @param status the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.STATUS
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * {@linkplain #flag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.FLAG
     */
    public String getFlag() {
        return flag;
    }

    /**
     * 
     * {@linkplain #flag}
     * @param flag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.FLAG
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * 
     * {@linkplain #hmManualFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.HM_MANUAL_FLAG
     */
    public String getHmManualFlag() {
        return hmManualFlag;
    }

    /**
     * 
     * {@linkplain #hmManualFlag}
     * @param hmManualFlag the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.HM_MANUAL_FLAG
     */
    public void setHmManualFlag(String hmManualFlag) {
        this.hmManualFlag = hmManualFlag;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #editor}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.EDITOR
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 
     * {@linkplain #editor}
     * @param editor the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.EDITOR
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 
     * {@linkplain #edittm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.EDITTM
     */
    public Date getEdittm() {
        return edittm;
    }

    /**
     * 
     * {@linkplain #edittm}
     * @param edittm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_SKU.EDITTM
     */
    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
    
}