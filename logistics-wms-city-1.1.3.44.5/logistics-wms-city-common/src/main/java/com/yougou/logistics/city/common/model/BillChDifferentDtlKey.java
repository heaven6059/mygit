package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillChDifferentDtlKey {
    private String locno;

    private String differentNo;

    private String cellNo;

    private String itemNo;

    private String barcode;

    private BigDecimal packQty;

    private String lotNo;

    private String quality;

    private Date produceDate;

    private Date expireDate;

    private String itemType;

    private String batchSerialNo;

    private String stockValue;

    private String labelNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getDifferentNo() {
        return differentNo;
    }

    public void setDifferentNo(String differentNo) {
        this.differentNo = differentNo;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

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

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }
}