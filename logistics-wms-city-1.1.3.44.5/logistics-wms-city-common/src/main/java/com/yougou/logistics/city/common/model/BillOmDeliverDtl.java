package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 装车单明细
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:15:11
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmDeliverDtl extends BillOmDeliverDtlKey {
	/**
	 * 出货类型
	 */
    private String expType;

    /**
     * 来源单号
     */
    private String expNo;

    /**
     * 波次号
     */
    private String locateNo;

    /**
     * 商品编码
     */
    private String itemNo;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 包装数量
     */
    private BigDecimal packQty;

    /**
     * 批号
     */
    private String lotNo;

    /**
     * 生成日期
     */
    private Date produceDate;
	/**
	 * 到期日
	 */
    private Date expireDate;

    /**
     * 品质
     */
    private String quality;

    /**
     * 验收批次
     */
    private String importBatchNo;

    /**
     * 批次流水号
     */
    private String batchSerialNo;

    /**
     * 扩展条码
     */
    private String extBarcodeNo;

    /**
     * 实际数量
     */
    private BigDecimal qty;

    private String creator;
    
    private String creatorname;

    private Date createtm;

    private String editor;
    
    private String editorname;

    private Date edittm;
    
    private String sizeNo;

    /**
     * 商品属性ID
     */
    private Long itemId;

    /**
     * 商品类型
     */
    private String itemType;

    /**
     * 出货日期
     */
    private Date expDate;

    /**
     * 操作状态
     */
    private String status;

    /**
     * 配送量
     */
    private BigDecimal realQty;

    /**
     * 序列号
     */
    private BigDecimal rowId;

    private String boxNo;

    private BigDecimal volume;

    private BigDecimal weight;

    private String storeNo;

    private String lineNo;

    private String m3TransStatus;
    
    private String itemName;
    
    private String storeName;
    
    /**  
     * 用于展示
     */
    private String brandNo;
    
    private String brandName;
    
    private String sysNo;
    
    private String recheckNo;
    
    private String circle;
    private String circleNo;
    
    private String loadproposeNo;
    
    private String tmsDeliverNo;//TMS配送单号
    
	public String getLoadproposeNo() {
		return loadproposeNo;
	}

	public void setLoadproposeNo(String loadproposeNo) {
		this.loadproposeNo = loadproposeNo;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getCircleNo() {
		return circleNo;
	}

	public void setCircleNo(String circleNo) {
		this.circleNo = circleNo;
	}

	public String getRecheckNo() {
		return recheckNo;
	}

	public void setRecheckNo(String recheckNo) {
		this.recheckNo = recheckNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getLocateNo() {
        return locateNo;
    }

    public void setLocateNo(String locateNo) {
        this.locateNo = locateNo;
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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public BigDecimal getRowId() {
        return rowId;
    }

    public void setRowId(BigDecimal rowId) {
        this.rowId = rowId;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getM3TransStatus() {
        return m3TransStatus;
    }

    public void setM3TransStatus(String m3TransStatus) {
        this.m3TransStatus = m3TransStatus;
    }

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTmsDeliverNo() {
		return tmsDeliverNo;
	}

	public void setTmsDeliverNo(String tmsDeliverNo) {
		this.tmsDeliverNo = tmsDeliverNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

}