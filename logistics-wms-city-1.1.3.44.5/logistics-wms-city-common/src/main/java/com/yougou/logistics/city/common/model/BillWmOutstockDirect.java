/*
 * 类名 com.yougou.logistics.city.common.model.BillWmOutstockDirect
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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

public class BillWmOutstockDirect extends BillWmOutstockDirectKey {
	
    private String locno;

    private String ownerNo;

    private String sourceNo;

    private String operateType;

    private String itemNo;

    private Long itemId;

    private BigDecimal packQty;

    private String sCellNo;

    private Long sCellId;

    private String sContainerNo;

    private String dCellNo;

    private Long dCellId;

    private String dContainerNo;

    private BigDecimal outstockItemQty;

    private String status;

    private String supplierNo;

    private String classType;

    private Short poId;

    private String stockType;

    private String stockValue;
    
    
    /**用于页面展示**/
    private String itemName;
    
    private String colorName;
    
    private String styleNo;
    
    private String sizeNo;
    
    private String boxNo;
    
    private String recedeNo;
    
    private String creator;

    private String brandNo;
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

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
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

    public BigDecimal getOutstockItemQty() {
        return outstockItemQty;
    }

    public void setOutstockItemQty(BigDecimal outstockItemQty) {
        this.outstockItemQty = outstockItemQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Short getPoId() {
        return poId;
    }

    public void setPoId(Short poId) {
        this.poId = poId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	
}