package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public class AccInventoryCon {
    /**
     * 容器编码
     */
    private String conNo;
    /**
     * 容器类型
     */
    private String conType;

    /**
     * 仓别
     */
    private String locno;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 储位编码
     */
    private String cellNo;

    /**
     * 商品属性类型
     */
    private String itemType;

    /**
     * 品质
     */
    private String quality;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 子容器数量
     */
    private BigDecimal childrenQty;

    /**
     * SKU数量
     */
    private BigDecimal skuQty;

    /**
     * 记帐人员
     */
    private String creator;

    /**
     * 记帐时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    /**
     * 修改人员
     */
    private String editor;

    /**
     * 修改时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;
    
    private String startCreatetm;
    private String endCreatetm;
    private String startEdittm;
    private String endEdittm;
    private String status;
    /**
     * 父级容器号
     */
    private String pConNo;
    private String bmStatus;//固定容器状态

    
    public String getBmStatus() {
		return bmStatus;
	}

	public void setBmStatus(String bmStatus) {
		this.bmStatus = bmStatus;
	}

	/**
     * 
     * {@linkplain #conNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CON_NO
     */
    public String getConNo() {
        return conNo;
    }

    /**
     * 
     * {@linkplain #conNo}
     * @param conNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CON_NO
     */
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CELL_NO
     */
    public String getCellNo() {
        return cellNo;
    }

    /**
     * 
     * {@linkplain #cellNo}
     * @param cellNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CELL_NO
     */
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #childrenQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CHILDREN_QTY
     */
    public BigDecimal getChildrenQty() {
        return childrenQty;
    }

    /**
     * 
     * {@linkplain #childrenQty}
     * @param childrenQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CHILDREN_QTY
     */
    public void setChildrenQty(BigDecimal childrenQty) {
        this.childrenQty = childrenQty;
    }

    /**
     * 
     * {@linkplain #skuQty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.SKU_QTY
     */
    public BigDecimal getSkuQty() {
        return skuQty;
    }

    /**
     * 
     * {@linkplain #skuQty}
     * @param skuQty the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.SKU_QTY
     */
    public void setSkuQty(BigDecimal skuQty) {
        this.skuQty = skuQty;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #editor}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.EDITOR
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 
     * {@linkplain #editor}
     * @param editor the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.EDITOR
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 
     * {@linkplain #edittm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.EDITTM
     */
    public Date getEdittm() {
        return edittm;
    }

    /**
     * 
     * {@linkplain #edittm}
     * @param edittm the value for USR_ZONE_WMS_DEV.ACC_INVENTORY_CON.EDITTM
     */
    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

	public String getStartCreatetm() {
		return startCreatetm;
	}

	public void setStartCreatetm(String startCreatetm) {
		this.startCreatetm = startCreatetm;
	}

	public String getEndCreatetm() {
		return endCreatetm;
	}

	public void setEndCreatetm(String endCreatetm) {
		this.endCreatetm = endCreatetm;
	}

	public String getStartEdittm() {
		return startEdittm;
	}

	public void setStartEdittm(String startEdittm) {
		this.startEdittm = startEdittm;
	}

	public String getEndEdittm() {
		return endEdittm;
	}

	public void setEndEdittm(String endEdittm) {
		this.endEdittm = endEdittm;
	}

	public String getConType() {
		return conType;
	}

	public void setConType(String conType) {
		this.conType = conType;
	}

	public String getpConNo() {
		return pConNo;
	}

	public void setpConNo(String pConNo) {
		this.pConNo = pConNo;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}