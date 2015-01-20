package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
public class BillContainerTaskDtl extends BillContainerTaskDtlKey {
    /**
     * 源容器号
     */
    private String sContainerNo;

    /**
     * 源子容器号
     */
    private String sSubContainerNo;

    /**
     * 目的容器号
     */
    private String dContainerNo;

    /**
     * 目的子容器号
     */
    private String dSubContainerNo;

    /**
     * 容器类型(目的),P-板,C-箱
     */
    private String containerType;

    /**
     * 源储位
     */
    private String sCellNo;

    /**
     * 目的储位
     */
    private String dCellNo;

    /**
     * 品牌
     */
    private String brandNo;

    /**
     * 商品编码
     */
    private String itemNo;

    /**
     * 尺码
     */
    private String sizeNo;

    /**
     * 数量
     */
    private Long qty;

    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建人名称
     */
    private String creatorname;

    /**
     * 创建时间
     */
    private Date createtm;

    /**
     * 修改人
     */
    private String editor;
    
    /**
     * 修改人名称
     */
    private String editorname;

    /**
     * 修改时间
     */
    private Date edittm;

    /**
     * 品质
     */
    private String quality;

    /**
     * 商品属性
     */
    private String itemType;

    /**
     * 
     * {@linkplain #sContainerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_CONTAINER_NO
     */
    public String getsContainerNo() {
        return sContainerNo;
    }

    /**
     * 
     * {@linkplain #sContainerNo}
     * @param sContainerNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_CONTAINER_NO
     */
    public void setsContainerNo(String sContainerNo) {
        this.sContainerNo = sContainerNo;
    }

    /**
     * 
     * {@linkplain #sSubContainerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_SUB_CONTAINER_NO
     */
    public String getsSubContainerNo() {
        return sSubContainerNo;
    }

    /**
     * 
     * {@linkplain #sSubContainerNo}
     * @param sSubContainerNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_SUB_CONTAINER_NO
     */
    public void setsSubContainerNo(String sSubContainerNo) {
        this.sSubContainerNo = sSubContainerNo;
    }

    /**
     * 
     * {@linkplain #dContainerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_CONTAINER_NO
     */
    public String getdContainerNo() {
        return dContainerNo;
    }

    /**
     * 
     * {@linkplain #dContainerNo}
     * @param dContainerNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_CONTAINER_NO
     */
    public void setdContainerNo(String dContainerNo) {
        this.dContainerNo = dContainerNo;
    }

    /**
     * 
     * {@linkplain #dSubContainerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_SUB_CONTAINER_NO
     */
    public String getdSubContainerNo() {
        return dSubContainerNo;
    }

    /**
     * 
     * {@linkplain #dSubContainerNo}
     * @param dSubContainerNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_SUB_CONTAINER_NO
     */
    public void setdSubContainerNo(String dSubContainerNo) {
        this.dSubContainerNo = dSubContainerNo;
    }

    /**
     * 
     * {@linkplain #containerType}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CONTAINER_TYPE
     */
    public String getContainerType() {
        return containerType;
    }

    /**
     * 
     * {@linkplain #containerType}
     * @param containerType the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CONTAINER_TYPE
     */
    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    /**
     * 
     * {@linkplain #sCellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_CELL_NO
     */
    public String getsCellNo() {
        return sCellNo;
    }

    /**
     * 
     * {@linkplain #sCellNo}
     * @param sCellNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.S_CELL_NO
     */
    public void setsCellNo(String sCellNo) {
        this.sCellNo = sCellNo;
    }

    /**
     * 
     * {@linkplain #dCellNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_CELL_NO
     */
    public String getdCellNo() {
        return dCellNo;
    }

    /**
     * 
     * {@linkplain #dCellNo}
     * @param dCellNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.D_CELL_NO
     */
    public void setdCellNo(String dCellNo) {
        this.dCellNo = dCellNo;
    }

    /**
     * 
     * {@linkplain #brandNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.BRAND_NO
     */
    public String getBrandNo() {
        return brandNo;
    }

    /**
     * 
     * {@linkplain #brandNo}
     * @param brandNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.BRAND_NO
     */
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ITEM_NO
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     * @param itemNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ITEM_NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 
     * {@linkplain #sizeNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.SIZE_NO
     */
    public String getSizeNo() {
        return sizeNo;
    }

    /**
     * 
     * {@linkplain #sizeNo}
     * @param sizeNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.SIZE_NO
     */
    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    /**
     * 
     * {@linkplain #qty}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.QTY
     */
    public Long getQty() {
        return qty;
    }

    /**
     * 
     * {@linkplain #qty}
     * @param qty the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.QTY
     */
    public void setQty(Long qty) {
        this.qty = qty;
    }

    /**
     * 
     * {@linkplain #creator}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * {@linkplain #creator}
     * @param creator the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #editor}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.EDITOR
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 
     * {@linkplain #editor}
     * @param editor the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.EDITOR
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 
     * {@linkplain #edittm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.EDITTM
     */
    public Date getEdittm() {
        return edittm;
    }

    /**
     * 
     * {@linkplain #edittm}
     * @param edittm the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.EDITTM
     */
    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
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