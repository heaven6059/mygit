/*
 * 类名 com.yougou.logistics.city.common.model.BillWmRequestDtl
 * @author yougoupublic
 * @date  Fri Mar 21 17:59:52 CST 2014
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

public class BillWmRequestDtl extends BillWmRequestDtlKey {
    private String status;

    private BigDecimal packQty;

    private BigDecimal itemQty;

    private String itemName;

    private String colorName;
    
    private String brandNo;

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
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

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

}