package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 容器操作日志
 * 
 * @author wanghb
 * @date 2014-8-16
 * @version 1.1.3.39
 */
public class BmContainerLog {
	
	//主表
	private String locNo;
	private String billNo;
	private String conNo;
	private String conType;
	private String subConNo;
	private String cellNo;
	private String userType;
	private String ioFlag;
	//明细表
	private String brandNo;
	private String brandName;
	private String itemNo;
	private String itemName;
	private String sizeNo;
	private Integer qty;
	private String creator;
	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date createtm;
	private String editor;
	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date edittm;
	private String creatorName;
	private String editorName;
	private String quality;
	private String itemType;
	private BigDecimal rowId;
	
	public String getLocNo() {
		return locNo;
	}

	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getConNo() {
		return conNo;
	}

	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
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

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
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
	public BigDecimal getRowId() {
		return rowId;
	}
	public void setRowId(BigDecimal rowId) {
		this.rowId = rowId;
	}
	public String getSubConNo() {
		return subConNo;
	}
	public void setSubConNo(String subConNo) {
		this.subConNo = subConNo;
	}
	public String getCellNo() {
		return cellNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getIoFlag() {
		return ioFlag;
	}
	public void setIoFlag(String ioFlag) {
		this.ioFlag = ioFlag;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getConType() {
		return conType;
	}
	public void setConType(String conType) {
		this.conType = conType;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	
}
