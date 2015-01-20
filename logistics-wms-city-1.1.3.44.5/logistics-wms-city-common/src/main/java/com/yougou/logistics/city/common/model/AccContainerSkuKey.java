package com.yougou.logistics.city.common.model;

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
public class AccContainerSkuKey {
    /**
     * 容器编码
     */
    private String conNo;

    /**
     * 仓别
     */
    private String locno;

    /**
     * 商品编码
     */
    private String itemNo;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 商品属性类型
     */
    private String itemType;

    /**
     * 品质
     */
    private String quality;

    /**
     * 
     * {@linkplain #conNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.CON_NO
     */
    public String getConNo() {
        return conNo;
    }

    /**
     * 
     * {@linkplain #conNo}
     * @param conNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.CON_NO
     */
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #itemNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.ITEM_NO
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * 
     * {@linkplain #itemNo}
     * @param itemNo the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.ITEM_NO
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 
     * {@linkplain #barcode}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.BARCODE
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 
     * {@linkplain #barcode}
     * @param barcode the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.BARCODE
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 
     * {@linkplain #itemType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.ITEM_TYPE
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 
     * {@linkplain #itemType}
     * @param itemType the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.ITEM_TYPE
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 
     * {@linkplain #quality}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.QUALITY
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 
     * {@linkplain #quality}
     * @param quality the value for USR_ZONE_WMS_DEV.ACC_CONTAINER_SKU.QUALITY
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }
}