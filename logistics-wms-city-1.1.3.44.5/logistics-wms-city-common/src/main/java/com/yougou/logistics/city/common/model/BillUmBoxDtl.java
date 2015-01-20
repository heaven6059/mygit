/*
 * 类名 com.yougou.logistics.city.common.model.BillUmBoxDtl
 * @author luo.hl
 * @date  Thu Jan 16 16:21:11 CST 2014
 * @version 1.0.6
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillUmBoxDtl extends BillUmBoxDtlKey {
	private Integer rowId;

	private BigDecimal itemQty;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date boxingDate;

	private String creator;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;
	
	private String creatorName;

	private String status;

	private String itemName;

	private String colorName;

	private String brandNo;
	
	private String itemType;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public Date getBoxingDate() {
		return boxingDate;
	}

	public void setBoxingDate(Date boxingDate) {
		this.boxingDate = boxingDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

}