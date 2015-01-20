/*
 * 类名 com.yougou.logistics.city.common.model.BillImInstockDtl
 * @author luo.hl
 * @date  Mon Sep 30 16:23:58 CST 2013
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

public class BillImInstockDtl extends BillImInstockDtlKey {
	private Long directSerial;

	private String instockType;

	private String cellNo;

	private Long cellId;

	private String containerNo;

	private String itemNo;

	private Long itemId;

	private BigDecimal packQty;

	private String destCellNo;

	private Long destCellId;

	private BigDecimal itemQty;

	private String realCellNo;

	private BigDecimal realQty;

	private String sourceNo;

	private String status;

	private BigDecimal workloadP;

	private BigDecimal workloadC;

	private BigDecimal workloadB;

	private String authorizedWorker;

	private String creator;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String midLabelNo;

	private String labelNo;

	private String sizeNo;

	private String itemType;

	private String quality;

	private String showItemType;

	private String showQuality;

	private BigDecimal instockedQty;

	private String brandNo;

	private String editorName;
	
	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getShowItemType() {
		return showItemType;
	}

	public void setShowItemType(String showItemType) {
		this.showItemType = showItemType;
	}

	public String getShowQuality() {
		return showQuality;
	}

	public void setShowQuality(String showQuality) {
		this.showQuality = showQuality;
	}

	public Long getDirectSerial() {
		return directSerial;
	}

	public void setDirectSerial(Long directSerial) {
		this.directSerial = directSerial;
	}

	public String getInstockType() {
		return instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
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

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public String getRealCellNo() {
		return realCellNo;
	}

	public void setRealCellNo(String realCellNo) {
		this.realCellNo = realCellNo;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getWorkloadP() {
		return workloadP;
	}

	public void setWorkloadP(BigDecimal workloadP) {
		this.workloadP = workloadP;
	}

	public BigDecimal getWorkloadC() {
		return workloadC;
	}

	public void setWorkloadC(BigDecimal workloadC) {
		this.workloadC = workloadC;
	}

	public BigDecimal getWorkloadB() {
		return workloadB;
	}

	public void setWorkloadB(BigDecimal workloadB) {
		this.workloadB = workloadB;
	}

	public String getAuthorizedWorker() {
		return authorizedWorker;
	}

	public void setAuthorizedWorker(String authorizedWorker) {
		this.authorizedWorker = authorizedWorker;
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

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.showItemType = SystemCache.getLookUpName("ITEM_TYPE", itemType);
		this.itemType = itemType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.showQuality = SystemCache.getLookUpName("AREA_QUALITY", quality);
		this.quality = quality;
	}

	public BigDecimal getInstockedQty() {
		return instockedQty;
	}

	public void setInstockedQty(BigDecimal instockedQty) {
		this.instockedQty = instockedQty;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

}