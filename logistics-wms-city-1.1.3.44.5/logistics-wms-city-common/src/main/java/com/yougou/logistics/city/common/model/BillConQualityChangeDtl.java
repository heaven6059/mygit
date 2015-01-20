/*
 * 类名 com.yougou.logistics.city.common.model.BillConQualityChangeDtl
 * @author luo.hl
 * @date  Thu Oct 24 13:54:46 CST 2013
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

public class BillConQualityChangeDtl extends BillConQualityChangeDtlKey {
    private String locno;

    private String ownerNo;

    private String sCellNo;

    private Long sCellId;

    private Long dCellId;

    private String itemNo;

    private String supplierNo;

    private String barcode;

    private BigDecimal packQty;

    private String lotNo;

    private Date produceDate;

    private Date expireDate;

    private String importBatchNo;

    private String batchSerialNo;

    private String extBarcodeNo;

    private String sQuality;

    private String dQuality;

    private BigDecimal changeQty;

    private String status;

    private BigDecimal realQty;

    private String itemType;

    private String stockType;

    private String stockValue;

    private String scanLabelNo;

    private String sizeNo;

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

    public String getsCellNo() {
        return sCellNo;
    }

    public void setsCellNo(String sCellNo) {
        this.sCellNo = sCellNo;
    }

    public Long getsCellId() {
        return sCellId;
    }

    public void setsCellId(Long sCellId) {
        this.sCellId = sCellId;
    }

    public Long getdCellId() {
        return dCellId;
    }

    public void setdCellId(Long dCellId) {
        this.dCellId = dCellId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
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

    public String getImportBatchNo() {
        return importBatchNo;
    }

    public void setImportBatchNo(String importBatchNo) {
        this.importBatchNo = importBatchNo;
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }

    public String getExtBarcodeNo() {
        return extBarcodeNo;
    }

    public void setExtBarcodeNo(String extBarcodeNo) {
        this.extBarcodeNo = extBarcodeNo;
    }

    public String getsQuality() {
        return sQuality;
    }

    public void setsQuality(String sQuality) {
        this.sQuality = sQuality;
    }

    public String getdQuality() {
        return dQuality;
    }

    public void setdQuality(String dQuality) {
        this.dQuality = dQuality;
    }

    public BigDecimal getChangeQty() {
        return changeQty;
    }

    public void setChangeQty(BigDecimal changeQty) {
        this.changeQty = changeQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRealQty() {
        return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
        this.realQty = realQty;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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

    public String getScanLabelNo() {
        return scanLabelNo;
    }

    public void setScanLabelNo(String scanLabelNo) {
        this.scanLabelNo = scanLabelNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }
}