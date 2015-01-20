package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-08-08 13:49:01
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
public class AccContainerSku extends AccContainerSkuKey {
    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 序号
     */
    private BigDecimal seqId;

    /**
     * 款号
     */
    private String styleNo;

    /**
     * 尺码
     */
    private String sizeNo;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 创建时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    /**
     * 更新时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date updatetm;
    /**
     * 商品编码
     */
    private String itemNo;
    private String itemName;
    /**
     * 容器编号
     */
    private String conNo;
    /**
     * 仓别
     */
    private String locno;
    /**
     * 条码
     */
    private String barcode;
    /**
     * 商品类型
     */
    private String itemType;
    /**
     * 品质
     */
    private String quality;
    private String status;
    private String brandNo;
    private String brandName;

    
    public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    /**
     * 
     * {@linkplain #seqId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SEQ_ID
     */
    public BigDecimal getSeqId() {
        return seqId;
    }

    /**
     * 
     * {@linkplain #seqId}
     * @param seqId the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SEQ_ID
     */
    public void setSeqId(BigDecimal seqId) {
        this.seqId = seqId;
    }

    /**
     * 
     * {@linkplain #styleNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.STYLE_NO
     */
    public String getStyleNo() {
        return styleNo;
    }

    /**
     * 
     * {@linkplain #styleNo}
     * @param styleNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.STYLE_NO
     */
    public void setStyleNo(String styleNo) {
        this.styleNo = styleNo;
    }

    /**
     * 
     * {@linkplain #sizeNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SIZE_NO
     */
    public String getSizeNo() {
        return sizeNo;
    }

    /**
     * 
     * {@linkplain #sizeNo}
     * @param sizeNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SIZE_NO
     */
    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    /**
     * 
     * {@linkplain #qty}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.QTY
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * 
     * {@linkplain #qty}
     * @param qty the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.QTY
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SUPPLIER_NO
     */
    public String getSupplierNo() {
        return supplierNo;
    }

    /**
     * 
     * {@linkplain #supplierNo}
     * @param supplierNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.SUPPLIER_NO
     */
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #updatetm}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.UPDATETM
     */
    public Date getUpdatetm() {
        return updatetm;
    }

    /**
     * 
     * {@linkplain #updatetm}
     * @param updatetm the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.UPDATETM
     */
    public void setUpdatetm(Date updatetm) {
        this.updatetm = updatetm;
    }

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getConNo() {
		return conNo;
	}

	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
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

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
    
}