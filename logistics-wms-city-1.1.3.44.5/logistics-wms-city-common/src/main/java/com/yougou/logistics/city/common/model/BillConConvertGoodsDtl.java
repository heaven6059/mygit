/*
 * 类名 com.yougou.logistics.city.common.model.BillConConvertGoodsDtl
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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

public class BillConConvertGoodsDtl extends BillConConvertGoodsDtlKey {
    private String checkNo;

    private String itemNo;

    private String sizeNo;

    private BigDecimal itemQty;

    private BigDecimal realQty;

    private String boxNo;

    private String status;

    private String brandNo;

    private Integer packQty;

    private String itemType;

    private String quality;

    private String sourceNo;

    private String businessType;

    private Date edittm;

    private String sCellNo;

    private String dCellNo;

    private String remark;
    
    private String colorName;
	
	private String brandName;
	
	private String storeName;
	
	private String itemName;
	
	private String supplierNo;

	private String barcode;
	
	private BigDecimal cellId;
	
	private BigDecimal contentQty;

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
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

    public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public BigDecimal getRealQty() {
        return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
        this.realQty = realQty;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public Integer getPackQty() {
        return packQty;
    }

    public void setPackQty(Integer packQty) {
        this.packQty = packQty;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getsCellNo() {
        return sCellNo;
    }

    public void setsCellNo(String sCellNo) {
        this.sCellNo = sCellNo;
    }

    public String getdCellNo() {
        return dCellNo;
    }

    public void setdCellNo(String dCellNo) {
        this.dCellNo = dCellNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public BigDecimal getCellId() {
		return cellId;
	}

	public void setCellId(BigDecimal cellId) {
		this.cellId = cellId;
	}

	public BigDecimal getContentQty() {
		return contentQty;
	}

	public void setContentQty(BigDecimal contentQty) {
		this.contentQty = contentQty;
	}
}