package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public class BillWmRecedeDtl extends BillWmRecedeDtlKey {
    private String ownerItemNo;

    private String itemNo;

    private String sizeNo;

    private BigDecimal packQty;

    private String lotNo;

    private String quality;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String reserved4;

    private Date produceDate;

    private Date expireDate;

    private BigDecimal recedeQty;

    private BigDecimal outstockQty;

    private BigDecimal realQty;

    private BigDecimal unitCost;

    private String recedeDes;

    private String errorStatus;

    private String batchSerialNo;

    private String boxNo;
    
    private String itemName;
    
    private String sysNo;
    
    private String barcode;
    
    private String supplierNo;
    
	/**
     * 款号
     */
    private String styleNo;
    
    /**
     * 颜色
     */
    private String  colorNoStr;
    
    /**
     * 品牌
     */
    private String  brandNoStr;
    
    private int conContentQty;//库存数量
    
    private String brandNo;//品牌编码
    
    public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorNoStr() {
		return colorNoStr;
	}

	public void setColorNoStr(String colorNoStr) {
		this.colorNoStr = colorNoStr;
	}

	public String getBrandNoStr() {
		return brandNoStr;
	}

	public void setBrandNoStr(String brandNoStr) {
		this.brandNoStr = brandNoStr;
	}

	public String getOwnerItemNo() {
        return ownerItemNo;
    }

    public void setOwnerItemNo(String ownerItemNo) {
        this.ownerItemNo = ownerItemNo;
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

    public BigDecimal getRecedeQty() {
        return recedeQty;
    }

    public void setRecedeQty(BigDecimal recedeQty) {
        this.recedeQty = recedeQty;
    }

    public BigDecimal getOutstockQty() {
        return outstockQty;
    }

    public void setOutstockQty(BigDecimal outstockQty) {
        this.outstockQty = outstockQty;
    }

    public BigDecimal getRealQty() {
        return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
        this.realQty = realQty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getRecedeDes() {
        return recedeDes;
    }

    public void setRecedeDes(String recedeDes) {
        this.recedeDes = recedeDes;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }
    public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public int getConContentQty() {
		return conContentQty;
	}

	public void setConContentQty(int conContentQty) {
		this.conContentQty = conContentQty;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
}