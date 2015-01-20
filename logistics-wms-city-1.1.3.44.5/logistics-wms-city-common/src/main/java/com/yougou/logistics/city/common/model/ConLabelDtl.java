/*
 * 类名 com.yougou.logistics.city.common.model.ConLabelDtl
 * @author qin.dy
 * @date  Mon Sep 30 15:10:42 CST 2013
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

public class ConLabelDtl extends ConLabelDtlKey {
    private String batchNo;

    private String ownerNo;

    private String sourceNo;

    private String containerType;

    private String itemNo;

    private Long itemId;

    private BigDecimal packQty;

    private BigDecimal qty;

    private String expNo;

    private String locateNo;

    private String storeNo;

    private String subStoreNo;

    private String lineNo;

    private String status;

    private Integer divideId;

    private String expType;

    private String dpsCellNo;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private String deliverObj;

    private Date expDate;

    private String advanceCellNo;

    private String advanceStatus;

    private String scanLabelNo;

    private String sizeNo;
    
    private Item item;
    
    private BigDecimal itemWeight;
    
    /**
     * 标签号 仅用于展示
     */
    private String labelNo;
    
    /**
     * 商品名称  仅用于展示
     */
    private String itemName;
    
    /**
     * 款号  仅用于展示
     */
    private String  styleNo;
    
    /**
     * 颜色  仅用于展示
     */
    private String  colorNoStr;
    
    /**
     * 通知单号  仅用于展示
     */
    private String recedeNo;
    
    /**
     * 复核单号  仅用于展示
     */
    private String recheckNo;
    
    
	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

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

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
	}

	public String getRecheckNo() {
		return recheckNo;
	}

	public void setRecheckNo(String recheckNo) {
		this.recheckNo = recheckNo;
	}

	public BigDecimal getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(BigDecimal itemWeight) {
		this.itemWeight = itemWeight;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getSubStoreNo() {
        return subStoreNo;
    }

    public void setSubStoreNo(String subStoreNo) {
        this.subStoreNo = subStoreNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDivideId() {
        return divideId;
    }

    public void setDivideId(Integer divideId) {
        this.divideId = divideId;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getDpsCellNo() {
        return dpsCellNo;
    }

    public void setDpsCellNo(String dpsCellNo) {
        this.dpsCellNo = dpsCellNo;
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

    public String getDeliverObj() {
        return deliverObj;
    }

    public void setDeliverObj(String deliverObj) {
        this.deliverObj = deliverObj;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getAdvanceCellNo() {
        return advanceCellNo;
    }

    public void setAdvanceCellNo(String advanceCellNo) {
        this.advanceCellNo = advanceCellNo;
    }

    public String getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(String advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public String getScanLabelNo() {
        return scanLabelNo;
    }

    public void setScanLabelNo(String scanLabelNo) {
        this.scanLabelNo = scanLabelNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }
}