package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public class BillUmDirect extends BillUmDirectKey {
	private String itemNo;

	private String sizeNo;

	private String cellNo;

	private BigDecimal estQty;

	private BigDecimal disQty;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private BigDecimal packQty;

	private BigDecimal cellId;

	private String status;

	private String ownerNo;

	private String itemName;

	private Integer flag;

	private String untreadType;

	private String quality;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

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

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public BigDecimal getEstQty() {
		return estQty;
	}

	public void setEstQty(BigDecimal estQty) {
		this.estQty = estQty;
	}

	public BigDecimal getDisQty() {
		return disQty;
	}

	public void setDisQty(BigDecimal disQty) {
		this.disQty = disQty;
	}

	public Date getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public BigDecimal getCellId() {
		return cellId;
	}

	public void setCellId(BigDecimal cellId) {
		this.cellId = cellId;
	}

	public String getUntreadType() {
		return untreadType;
	}

	public void setUntreadType(String untreadType) {
		this.untreadType = untreadType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

}