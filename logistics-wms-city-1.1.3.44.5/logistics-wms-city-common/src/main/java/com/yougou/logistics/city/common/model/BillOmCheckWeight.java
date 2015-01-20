package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 称重
 * 
 * @author qin.dy
 * @date 2013-9-29 下午9:33:17
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmCheckWeight extends BillOmCheckWeightKey {
    
	private String containerType;
	
	private String itemNo;
	
	private String itemName;
	
	private String sizeNo;
	
	private String qty;
	
	private String colorNo;
	
	private String colorName;
	
	private String styleNo;
	
	private String storeNo;

    private BigDecimal realWeight;

    private String operateWorker;

    private Date operateDate;

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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
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

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public BigDecimal getRealWeight() {
		return realWeight;
	}

	public void setRealWeight(BigDecimal realWeight) {
		this.realWeight = realWeight;
	}

	public String getOperateWorker() {
		return operateWorker;
	}

	public void setOperateWorker(String operateWorker) {
		this.operateWorker = operateWorker;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
    
}