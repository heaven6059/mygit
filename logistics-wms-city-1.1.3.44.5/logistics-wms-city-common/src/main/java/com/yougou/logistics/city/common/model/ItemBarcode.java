/*
 * 类名 com.yougou.logistics.city.common.model.ItemBarcode
 * @author qin.dy
 * @date  Sat Nov 09 15:00:22 CST 2013
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

import com.yougou.logistics.city.common.enums.BaseInfoItemBarcodePackageIdEnums;

public class ItemBarcode extends ItemBarcodeKey {
    private Short packageId;

    private String packUnit;

    private String packSpec;

    private Short skuL;

    private Short skuW;

    private Short skuH;

    private BigDecimal salePrice;

    private String creator;

    private Date createtm;

    private String remarks;

    private Date edittm;

    private String editor;

    private Date recievetm;

    private String primaryFlag;

    private String sysNo;
    
    private String barcodeTypeStr;//条码类型

    public Short getPackageId() {
        return packageId;
    }

    public void setPackageId(Short packageId) {
        this.packageId = packageId;
        this.barcodeTypeStr = BaseInfoItemBarcodePackageIdEnums.getTextByStatus(String.valueOf(this.packageId));
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public String getPackSpec() {
        return packSpec;
    }

    public void setPackSpec(String packSpec) {
        this.packSpec = packSpec;
    }

    public Short getSkuL() {
        return skuL;
    }

    public void setSkuL(Short skuL) {
        this.skuL = skuL;
    }

    public Short getSkuW() {
        return skuW;
    }

    public void setSkuW(Short skuW) {
        this.skuW = skuW;
    }

    public Short getSkuH() {
        return skuH;
    }

    public void setSkuH(Short skuH) {
        this.skuH = skuH;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getRecievetm() {
        return recievetm;
    }

    public void setRecievetm(Date recievetm) {
        this.recievetm = recievetm;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

	public String getBarcodeTypeStr() {
		return barcodeTypeStr;
	}

	public void setBarcodeTypeStr(String barcodeTypeStr) {
		this.barcodeTypeStr = barcodeTypeStr;
	}
}