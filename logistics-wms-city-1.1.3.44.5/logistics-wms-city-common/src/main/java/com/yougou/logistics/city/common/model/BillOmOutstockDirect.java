package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
public class BillOmOutstockDirect extends BillOmOutstockDirectKey {
	private String ownerNo;

	private String outstockType;

	private String operateType;

	private String pickType;

	private String batchNo;

	private String locateNo;

	private String expType;

	private String expNo;

	private String storeNo;

	private String subStoreNo;

	private String itemNo;

	private String sizeNo;

	private Long itemId;

	private BigDecimal packQty;

	private String sCellNo;

	private Long sCellId;

	private String sContainerNo;

	private String dCellNo;

	private Long dCellId;

	private String pickContainerNo;

	private String custContainerNo;

	private BigDecimal itemQty;

	private BigDecimal locateQty;

	private String status;

	private String deliverArea;

	private String lineNo;

	private Short priority;

	private String aSorterChuteNo;

	private String checkChuteNo;

	private String deliverObj;

	private Long suppCount;

	private String equipmentNo;

	private String dpsCellNo;

	private String creator;
	
	private String creatorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	
	private String editorname;

	private Date edittm;

	private String tempStatus;

	private Date expDate;

	private String stockType;

	private String scanLabelNo;

	private String emptyFlag;

	private String labelpickFlag;

	private String quality;

	private String massFlag;
	
	private BigDecimal workQty;

	private String itemType;

	/**
	 * 辅助字段，用于页面展示
	 */
	private String itemname;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String storeName;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String areaName;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String areaNo;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String styleNo;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String colorNo;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String commdityName;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String colorName;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String cellType;
	/**
	 * 辅助字段，用于页面展示
	 */
	private int itemTotalQty;
	/**
	 * 辅助字段，用于页面展示
	 */
	private int locateTotalQty;
	/**
	 * 辅助字段，用于页面展示
	 */
	private String brandNo;
	
	private int sumQty;
	
	private String supplierNo;

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	public String getCommdityName() {
		return commdityName;
	}

	public void setCommdityName(String commdityName) {
		this.commdityName = commdityName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getOutstockType() {
		return outstockType;
	}

	public void setOutstockType(String outstockType) {
		this.outstockType = outstockType;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getPickType() {
		return pickType;
	}

	public void setPickType(String pickType) {
		this.pickType = pickType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLocateNo() {
		return locateNo;
	}

	public void setLocateNo(String locateNo) {
		this.locateNo = locateNo;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getSubStoreNo() {
		return subStoreNo;
	}

	public void setSubStoreNo(String subStoreNo) {
		this.subStoreNo = subStoreNo;
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

	public String getsCellNo() {
		return sCellNo;
	}

	public void setsCellNo(String sCellNo) {
		this.sCellNo = sCellNo;
	}

	public Long getsCellId() {
		return sCellId;
	}

	public void setsCellId(Long sCellId) {
		this.sCellId = sCellId;
	}

	public String getsContainerNo() {
		return sContainerNo;
	}

	public void setsContainerNo(String sContainerNo) {
		this.sContainerNo = sContainerNo;
	}

	public String getdCellNo() {
		return dCellNo;
	}

	public void setdCellNo(String dCellNo) {
		this.dCellNo = dCellNo;
	}

	public Long getdCellId() {
		return dCellId;
	}

	public void setdCellId(Long dCellId) {
		this.dCellId = dCellId;
	}

	public String getPickContainerNo() {
		return pickContainerNo;
	}

	public void setPickContainerNo(String pickContainerNo) {
		this.pickContainerNo = pickContainerNo;
	}

	public String getCustContainerNo() {
		return custContainerNo;
	}

	public void setCustContainerNo(String custContainerNo) {
		this.custContainerNo = custContainerNo;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getLocateQty() {
		return locateQty;
	}

	public void setLocateQty(BigDecimal locateQty) {
		this.locateQty = locateQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliverArea() {
		return deliverArea;
	}

	public void setDeliverArea(String deliverArea) {
		this.deliverArea = deliverArea;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public Short getPriority() {
		return priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public String getaSorterChuteNo() {
		return aSorterChuteNo;
	}

	public void setaSorterChuteNo(String aSorterChuteNo) {
		this.aSorterChuteNo = aSorterChuteNo;
	}

	public String getCheckChuteNo() {
		return checkChuteNo;
	}

	public void setCheckChuteNo(String checkChuteNo) {
		this.checkChuteNo = checkChuteNo;
	}

	public String getDeliverObj() {
		return deliverObj;
	}

	public void setDeliverObj(String deliverObj) {
		this.deliverObj = deliverObj;
	}

	public Long getSuppCount() {
		return suppCount;
	}

	public void setSuppCount(Long suppCount) {
		this.suppCount = suppCount;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getDpsCellNo() {
		return dpsCellNo;
	}

	public void setDpsCellNo(String dpsCellNo) {
		this.dpsCellNo = dpsCellNo;
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

	public String getTempStatus() {
		return tempStatus;
	}

	public void setTempStatus(String tempStatus) {
		this.tempStatus = tempStatus;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getScanLabelNo() {
		return scanLabelNo;
	}

	public void setScanLabelNo(String scanLabelNo) {
		this.scanLabelNo = scanLabelNo;
	}

	public String getEmptyFlag() {
		return emptyFlag;
	}

	public void setEmptyFlag(String emptyFlag) {
		this.emptyFlag = emptyFlag;
	}

	public String getLabelpickFlag() {
		return labelpickFlag;
	}

	public void setLabelpickFlag(String labelpickFlag) {
		this.labelpickFlag = labelpickFlag;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getMassFlag() {
		return massFlag;
	}

	public void setMassFlag(String massFlag) {
		this.massFlag = massFlag;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public int getItemTotalQty() {
		return itemTotalQty;
	}

	public void setItemTotalQty(int itemTotalQty) {
		this.itemTotalQty = itemTotalQty;
	}

	public int getLocateTotalQty() {
		return locateTotalQty;
	}

	public void setLocateTotalQty(int locateTotalQty) {
		this.locateTotalQty = locateTotalQty;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public BigDecimal getWorkQty() {
		return workQty;
	}

	public void setWorkQty(BigDecimal workQty) {
		this.workQty = workQty;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public int getSumQty() {
		return sumQty;
	}

	public void setSumQty(int sumQty) {
		this.sumQty = sumQty;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}
	
}