package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
public class BillOmExpDtl extends BillOmExpDtlKey {
    private BigDecimal itemQty;

    private BigDecimal scheduleQty;

    private BigDecimal locateQty;

    private BigDecimal realQty;

    private BigDecimal unitCost;

    private String ownerItemNo;

    private String status;

    private String errorStatus;

    private Date createtm;

    private String itemType;

    private Date expDate;

    private String quality;

    private BigDecimal leaveQty;

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
    private String brandNo;
    private String  brandNoStr;
    
    /**
     * 条码
     */
    private String tBarcode;
    
    /**
     * 商品名称
     */
    private String itemName;
    
    /**
     * 客户编码
     */
    private String storeNo;
    /**
     * 客户名称
     */
    private String storeName;
    
    private String tt;
    
    
    /**
     * 合同号
     */
    private String poNo;
    
    /**
     * 装车出库数量
     */
    private BigDecimal deliverQty;
    
    private String sourceNo;
    
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

	public String gettBarcode() {
		return tBarcode;
	}

	public void settBarcode(String tBarcode) {
		this.tBarcode = tBarcode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public BigDecimal getScheduleQty() {
        return scheduleQty;
    }

    public void setScheduleQty(BigDecimal scheduleQty) {
        this.scheduleQty = scheduleQty;
    }

    public BigDecimal getLocateQty() {
        return locateQty;
    }

    public void setLocateQty(BigDecimal locateQty) {
        this.locateQty = locateQty;
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

    public String getOwnerItemNo() {
        return ownerItemNo;
    }

    public void setOwnerItemNo(String ownerItemNo) {
        this.ownerItemNo = ownerItemNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public Date getCreatetm() {
        return createtm;
    }

    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public BigDecimal getLeaveQty() {
        return leaveQty;
    }

    public void setLeaveQty(BigDecimal leaveQty) {
        this.leaveQty = leaveQty;
    }

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getTt() {
		return tt;
	}

	public void setTt(String tt) {
		this.tt = tt;
	}

	public BigDecimal getDeliverQty() {
		return deliverQty;
	}

	public void setDeliverQty(BigDecimal deliverQty) {
		this.deliverQty = deliverQty;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
}