package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
public class BillOmDivideDifferentDtl extends BillOmDivideDifferentDtlKey {
    private Date operateDate;

    private String storeNo;

    private String sItemNo;

    private BigDecimal packQty;

    private BigDecimal itemQty;

    private BigDecimal realQty;

    private String sCellNo;

    private Long sCellId;

    private String sContainerNo;

    private String dCellNo;

    private Long dCellId;

    private String dContainerNo;

    private String status;

    private String boxNo;

    private String dItemNo;

    private String brandNo;

    private String sBarcode;

    private String dBarcode;

    private BigDecimal pixFlag;
    
    private String ownerNo;
    
    private String expNo;
    
    
    /**
     * 临时属性
     */
    private String sSizeNo;
    
    private String dSizeNo;
    
    private String supplierNo;
    
    private String itemType;
    
    private String quality;
    
    private String sItemName;
    
    private String dItemName;
    
    private String brandName;
    
    private String checkStatus;
    
    private String pixFlagStr;
    
    private String dSupplierNo;
    
    private String sourceNo;

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getsItemNo() {
        return sItemNo;
    }

    public void setsItemNo(String sItemNo) {
        this.sItemNo = sItemNo;
    }

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
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

    public String getsContainerNo() {
        return sContainerNo;
    }

    public void setsContainerNo(String sContainerNo) {
        this.sContainerNo = sContainerNo;
    }

    public String getdCellNo() {
        return dCellNo;
    }

    public void setdCellNo(String dCellNo) {
        this.dCellNo = dCellNo;
    }

    public Long getdCellId() {
        return dCellId;
    }

    public void setdCellId(Long dCellId) {
        this.dCellId = dCellId;
    }

    public String getdContainerNo() {
        return dContainerNo;
    }

    public void setdContainerNo(String dContainerNo) {
        this.dContainerNo = dContainerNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getdItemNo() {
        return dItemNo;
    }

    public void setdItemNo(String dItemNo) {
        this.dItemNo = dItemNo;
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public String getsBarcode() {
        return sBarcode;
    }

    public void setsBarcode(String sBarcode) {
        this.sBarcode = sBarcode;
    }

    public String getdBarcode() {
        return dBarcode;
    }

    public void setdBarcode(String dBarcode) {
        this.dBarcode = dBarcode;
    }

    public BigDecimal getPixFlag() {
        return pixFlag;
    }

    public void setPixFlag(BigDecimal pixFlag) {
        this.pixFlag = pixFlag;
    }

	public String getsSizeNo() {
		return sSizeNo;
	}

	public void setsSizeNo(String sSizeNo) {
		this.sSizeNo = sSizeNo;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
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

	public String getsItemName() {
		return sItemName;
	}

	public void setsItemName(String sItemName) {
		this.sItemName = sItemName;
	}

	public String getdItemName() {
		return dItemName;
	}

	public void setdItemName(String dItemName) {
		this.dItemName = dItemName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getdSizeNo() {
		return dSizeNo;
	}

	public void setdSizeNo(String dSizeNo) {
		this.dSizeNo = dSizeNo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getPixFlagStr() {
		return pixFlagStr;
	}

	public void setPixFlagStr(String pixFlagStr) {
		this.pixFlagStr = pixFlagStr;
	}

	public String getdSupplierNo() {
		return dSupplierNo;
	}

	public void setdSupplierNo(String dSupplierNo) {
		this.dSupplierNo = dSupplierNo;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}
}