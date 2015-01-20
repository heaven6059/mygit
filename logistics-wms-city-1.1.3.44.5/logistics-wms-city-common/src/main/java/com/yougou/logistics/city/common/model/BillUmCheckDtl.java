/*
 * 类名 com.yougou.logistics.city.common.model.BillUmCheckDtl
 * @author luo.hl
 * @date  Wed Jan 15 16:39:22 CST 2014
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

public class BillUmCheckDtl extends BillUmCheckDtlKey {
	private String itemNo;

	private String itemName;

	private String colorName;

	private String sizeNo;

	private BigDecimal itemQty;

	private BigDecimal checkQty;

	private String status;

	private String boxNo;

	private String brandNo;
	
	private String brandName;

	private String addFlag;

	private BigDecimal difQty;

	private String untreadMmNo;
	
	private String untreadNo;
	
	private String quality;
	
	private String itemType;
	
	private BigDecimal recheckQty;
	
	private BigDecimal noRecheckQty;
	
	private String editor;
	private String editorName;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;
	
	public String getUntreadMmNo() {
		return untreadMmNo;
	}

	public void setUntreadMmNo(String untreadMmNo) {
		this.untreadMmNo = untreadMmNo;
	}

	public BigDecimal getDifQty() {
		return difQty;
	}

	public void setDifQty(BigDecimal difQty) {
		this.difQty = difQty;
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

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public String getUntreadNo() {
		return untreadNo;
	}

	public void setUntreadNo(String untreadNo) {
		this.untreadNo = untreadNo;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public BigDecimal getNoRecheckQty() {
		return noRecheckQty;
	}

	public void setNoRecheckQty(BigDecimal noRecheckQty) {
		this.noRecheckQty = noRecheckQty;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}
	
}