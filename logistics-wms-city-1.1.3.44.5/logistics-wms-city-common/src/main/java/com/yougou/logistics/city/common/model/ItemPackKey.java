/*
 * 类名 com.yougou.logistics.city.common.model.ItemPackKey
 * @author luo.hl
 * @date  Wed Oct 09 19:26:37 CST 2013
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

public class ItemPackKey {
    private String itemNo;

    private String sizeNo;

    private Short packQty;

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

    public Short getPackQty() {
        return packQty;
    }

    public void setPackQty(Short packQty) {
        this.packQty = packQty;
    }
}