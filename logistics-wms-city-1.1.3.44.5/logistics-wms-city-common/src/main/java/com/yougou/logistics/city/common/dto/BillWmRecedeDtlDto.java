package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public class BillWmRecedeDtlDto extends BaseSizeType {
	
    private String recedeNo;
    
    private String itemNo;

    private String itemName;

    private String sizeNo;

    private String sysNo;

    private String sizeKind;
   
    private BigDecimal recedeQty;
    
    private BigDecimal allCount;

    private BigDecimal allCost;
   
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
    private String  brandNoStr;

    public BigDecimal getAllCount() {
		return allCount;
	}

	public void setAllCount(BigDecimal allCount) {
		this.allCount = allCount;
	}

	public BigDecimal getAllCost() {
		return allCost;
	}

	public void setAllCost(BigDecimal allCost) {
		this.allCost = allCost;
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

	public String getBrandNoStr() {
		return brandNoStr;
	}

	public void setBrandNoStr(String brandNoStr) {
		this.brandNoStr = brandNoStr;
	}

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
	}


	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public BigDecimal getRecedeQty() {
		return recedeQty;
	}

	public void setRecedeQty(BigDecimal recedeQty) {
		this.recedeQty = recedeQty;
	}

}