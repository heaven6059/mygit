/*
 * 类名 com.yougou.logistics.city.common.model.BillImCheckPal
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

import java.math.BigDecimal;
import java.util.Date;

public class BillImCheckPal extends BillImCheckPalKey {
    private String itemNo;

    private String barcode;

    private String quality;

    private Date produceDate;

    private Date expireDate;

    private String lotNo;

    private BigDecimal price;

    private BigDecimal packQty;

    private BigDecimal checkQty;

    private String status;

    private String fixpalFlag;

    private String printerGroupNo;

    private String dockNo;

    private String storeNo;

    private String subStoreNo;

    private String stockType;

    private String stockValue;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private String itemType;

    private String businessType;

    private String sizeNo;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    public BigDecimal getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(BigDecimal checkQty) {
        this.checkQty = checkQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFixpalFlag() {
        return fixpalFlag;
    }

    public void setFixpalFlag(String fixpalFlag) {
        this.fixpalFlag = fixpalFlag;
    }

    public String getPrinterGroupNo() {
        return printerGroupNo;
    }

    public void setPrinterGroupNo(String printerGroupNo) {
        this.printerGroupNo = printerGroupNo;
    }

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getSubStoreNo() {
        return subStoreNo;
    }

    public void setSubStoreNo(String subStoreNo) {
        this.subStoreNo = subStoreNo;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatetm() {
        return createtm;
    }

    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }
}