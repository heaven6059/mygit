package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 请写出类的用途
 * 
 * @author chen.yl1
 * @date 2014-01-15 17:53:08
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public class BillConAdjDtl extends BillConAdjDtlKey {
	private String ownerNo;

	private String itemNo;

	private String sizeNo;

	private BigDecimal packQty;

	private String supplierNo;

	private String lotNo;

	private String reserved1;

	private String reserved2;

	private String reserved3;

	private String reserved4;

	private Date produceDate;

	private Date expireDate;

	private String quality;
	
	private String qualityStr;

	private String batchSerialNo;

	private String extBarcodeNo;

	private String cellNo;

	private BigDecimal planQty;

	private BigDecimal adjQty;

	private BigDecimal realQty;

	private String adjDes;

	private Short sRowId;

	private String importBatchNo;

	private String barcode;

	private String itemType;
	
	private String itemTypeStr;

	private String stockType;

	private String stockValue;

	private String labelNo;

	/** 商品名称 **/
	private String itemName;

	/** 品牌 **/
	private String brandName;

	/** 颜色 **/
	private String colorNo;
	private String color;

	private String sysNo;
	private String sysNOName;
	private String yearsStr;
	private String seasonStr;
	private String genderStr;
	private String editor;
    private String editorName;
    private Date edittm;

	/** 库存数量 **/
	private BigDecimal conQty;
	/** 库存ID **/
    private String conId;
    
    private String brandNo;
    
    private String dCellNo;
    private String panNo;
    
    
	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSysNOName() {
		return sysNOName;
	}

	public void setSysNOName(String sysNOName) {
		this.sysNOName = sysNOName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	public BigDecimal getConQty() {
		return conQty;
	}

	public void setConQty(BigDecimal conQty) {
		this.conQty = conQty;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

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

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getQualityStr() {
		return qualityStr;
	}

	public void setQualityStr(String qualityStr) {
		this.qualityStr = qualityStr;
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

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public BigDecimal getPlanQty() {
		return planQty;
	}

	public void setPlanQty(BigDecimal planQty) {
		this.planQty = planQty;
	}

	public BigDecimal getAdjQty() {
		return adjQty;
	}

	public void setAdjQty(BigDecimal adjQty) {
		this.adjQty = adjQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public String getAdjDes() {
		return adjDes;
	}

	public void setAdjDes(String adjDes) {
		this.adjDes = adjDes;
	}

	public Short getsRowId() {
		return sRowId;
	}

	public void setsRowId(Short sRowId) {
		this.sRowId = sRowId;
	}

	public String getImportBatchNo() {
		return importBatchNo;
	}

	public void setImportBatchNo(String importBatchNo) {
		this.importBatchNo = importBatchNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeStr() {
		return itemTypeStr;
	}

	public void setItemTypeStr(String itemTypeStr) {
		this.itemTypeStr = itemTypeStr;
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

	public String getLabelNo() {
		if(StringUtils.isNotEmpty(labelNo) && labelNo.equals("N")){
			return "";
		}
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getYearsStr() {
		return yearsStr;
	}

	public void setYearsStr(String yearsStr) {
		this.yearsStr = yearsStr;
	}

	public String getSeasonStr() {
		return seasonStr;
	}

	public void setSeasonStr(String seasonStr) {
		this.seasonStr = seasonStr;
	}

	public String getGenderStr() {
		return genderStr;
	}

	public void setGenderStr(String genderStr) {
		this.genderStr = genderStr;
	}

	public String getdCellNo() {
		return dCellNo;
	}

	public void setdCellNo(String dCellNo) {
		this.dCellNo = dCellNo;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}
	
}