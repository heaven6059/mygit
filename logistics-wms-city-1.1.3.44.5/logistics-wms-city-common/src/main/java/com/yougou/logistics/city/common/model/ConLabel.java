/*
 * 类名 com.yougou.logistics.city.common.model.ConLabel
 * @author qin.dy
 * @date  Mon Sep 30 15:09:38 CST 2013
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
import java.util.List;

public class ConLabel extends ConLabelKey {
    private String batchNo;

    private String sourceNo;

    private String deliverArea;

    private String status;

    private String loadContainerNo;

    private String ownerContainerNo;

    private String ownerCellNo;

    private String storeNo;

    private String trunckCellNo;

    private String aSorterChuteNo;

    private String checkChuteNo;

    private String deliverObj;

    private String useType;

    private String lineNo;

    private String currArea;

    private Short seqValue;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private String equipmentNo;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private String reportId;

    private String recheckNo;

    private String midLabelNo;

    private String bigExpNoFlag;

    private String checkChuteInstatus;

    private String scanLabelNo;

    private String stockType;

    private Date expDate;

    private String chuteLabelFlag;

    private String hmManualFlag;

    
    private List<ConLabelDtl> lableDtl;
    
    public List<ConLabelDtl> getLableDtl() {
		return lableDtl;
	}

	public void setLableDtl(List<ConLabelDtl> lableDtl) {
		this.lableDtl = lableDtl;
	}

	public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getDeliverArea() {
        return deliverArea;
    }

    public void setDeliverArea(String deliverArea) {
        this.deliverArea = deliverArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoadContainerNo() {
        return loadContainerNo;
    }

    public void setLoadContainerNo(String loadContainerNo) {
        this.loadContainerNo = loadContainerNo;
    }

    public String getOwnerContainerNo() {
        return ownerContainerNo;
    }

    public void setOwnerContainerNo(String ownerContainerNo) {
        this.ownerContainerNo = ownerContainerNo;
    }

    public String getOwnerCellNo() {
        return ownerCellNo;
    }

    public void setOwnerCellNo(String ownerCellNo) {
        this.ownerCellNo = ownerCellNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getTrunckCellNo() {
        return trunckCellNo;
    }

    public void setTrunckCellNo(String trunckCellNo) {
        this.trunckCellNo = trunckCellNo;
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

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getCurrArea() {
        return currArea;
    }

    public void setCurrArea(String currArea) {
        this.currArea = currArea;
    }

    public Short getSeqValue() {
        return seqValue;
    }

    public void setSeqValue(Short seqValue) {
        this.seqValue = seqValue;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
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

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getRecheckNo() {
        return recheckNo;
    }

    public void setRecheckNo(String recheckNo) {
        this.recheckNo = recheckNo;
    }

    public String getMidLabelNo() {
        return midLabelNo;
    }

    public void setMidLabelNo(String midLabelNo) {
        this.midLabelNo = midLabelNo;
    }

    public String getBigExpNoFlag() {
        return bigExpNoFlag;
    }

    public void setBigExpNoFlag(String bigExpNoFlag) {
        this.bigExpNoFlag = bigExpNoFlag;
    }

    public String getCheckChuteInstatus() {
        return checkChuteInstatus;
    }

    public void setCheckChuteInstatus(String checkChuteInstatus) {
        this.checkChuteInstatus = checkChuteInstatus;
    }

    public String getScanLabelNo() {
        return scanLabelNo;
    }

    public void setScanLabelNo(String scanLabelNo) {
        this.scanLabelNo = scanLabelNo;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getChuteLabelFlag() {
        return chuteLabelFlag;
    }

    public void setChuteLabelFlag(String chuteLabelFlag) {
        this.chuteLabelFlag = chuteLabelFlag;
    }

    public String getHmManualFlag() {
        return hmManualFlag;
    }

    public void setHmManualFlag(String hmManualFlag) {
        this.hmManualFlag = hmManualFlag;
    }
}