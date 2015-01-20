/*
 * 类名 com.yougou.logistics.city.common.model.BillImInstockDirect
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
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
import java.util.Date;

public class BillImInstockDirect extends BillImInstockDirectKey {
	private String ownerNo;

	private String locateType;

	private Long locateRowId;

	private String sourceNo;

	private String autoLocateFlag;

	private String operateType;

	private String cellNo;

	private Long cellId;

	private String containerNo;

	private String itemNo;

	private String itemName;

	private String styleNo;

	private String unitName;

	private Long itemId;

	private BigDecimal packQty;

	private String destCellNo;

	private Long destCellId;

	private String destContainerNo;

	private BigDecimal instockQty;

	private String status;

	private String creator;

	private Date createtm;

	private String editor;

	private Date edittm;

	private String instockType;

	private String checkChuteNo;

	private String containerLocateFlag;

	private String stockType;

	private String stockValue;

	private String midLabelNo;

	private String labelNo;

	private String sysNo;

	private String sizeNo;
	
    private String itemType;

    private String quality;
    
	private String boxNo;
	
	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public Long getLocateRowId() {
		return locateRowId;
	}

	public void setLocateRowId(Long locateRowId) {
		this.locateRowId = locateRowId;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getAutoLocateFlag() {
		return autoLocateFlag;
	}

	public void setAutoLocateFlag(String autoLocateFlag) {
		this.autoLocateFlag = autoLocateFlag;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Long getCellId() {
		return cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getDestCellNo() {
		return destCellNo;
	}

	public void setDestCellNo(String destCellNo) {
		this.destCellNo = destCellNo;
	}

	public Long getDestCellId() {
		return destCellId;
	}

	public void setDestCellId(Long destCellId) {
		this.destCellId = destCellId;
	}

	public String getDestContainerNo() {
		return destContainerNo;
	}

	public void setDestContainerNo(String destContainerNo) {
		this.destContainerNo = destContainerNo;
	}

	public BigDecimal getInstockQty() {
		return instockQty;
	}

	public void setInstockQty(BigDecimal instockQty) {
		this.instockQty = instockQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getInstockType() {
		return instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}

	public String getCheckChuteNo() {
		return checkChuteNo;
	}

	public void setCheckChuteNo(String checkChuteNo) {
		this.checkChuteNo = checkChuteNo;
	}

	public String getContainerLocateFlag() {
		return containerLocateFlag;
	}

	public void setContainerLocateFlag(String containerLocateFlag) {
		this.containerLocateFlag = containerLocateFlag;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStockValue() {
		return stockValue;
	}

	public void setStockValue(String stockValue) {
		this.stockValue = stockValue;
	}

	public String getMidLabelNo() {
		return midLabelNo;
	}

	public void setMidLabelNo(String midLabelNo) {
		this.midLabelNo = midLabelNo;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

}