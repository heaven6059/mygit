/*
 * 类名 com.yougou.logistics.city.common.model.BillUmCheck
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

public class BillUmCheck extends BillUmCheckKey {
	private String untreadNo;

	private String loadboxNo;

	private String instockDirectNo;

	private String status;

	private String itemType;

	private String quality;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	private Date edittm;

	private String auditor;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	private String remark;
	
	private String convertType;

	private String statusTrans;

	private String untreadMmNo;

	private int dtlCount;

	private int checkCount;

	private String sourceStatus;

	private String storeNo;
	private String storeName;
	private String poNo;

	private BigDecimal itemQty;
	private BigDecimal realQty;
	private BigDecimal checkQty;

	private String sourceNo;
	private String itemTypeStr;
	private String qualityStr;
	private String convertTypeStr;
	private String sysNo;

	private String checkStatus;
	//退仓装箱时 检查退仓验收状态
	private String checkFlagStatus;
	
	private String creatorName;
	private String editorName;
	private String auditorName;
	
	private String checkType;
	    
	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getUntreadNo() {
		return untreadNo;
	}

	public void setUntreadNo(String untreadNo) {
		this.untreadNo = untreadNo;
	}

	public String getLoadboxNo() {
		return loadboxNo;
	}

	public void setLoadboxNo(String loadboxNo) {
		this.loadboxNo = loadboxNo;
	}

	public String getInstockDirectNo() {
		return instockDirectNo;
	}

	public void setInstockDirectNo(String instockDirectNo) {
		this.instockDirectNo = instockDirectNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAudittm() {
		return audittm;
	}

	public void setAudittm(Date audittm) {
		this.audittm = audittm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatusTrans() {
		return statusTrans;
	}

	public void setStatusTrans(String statusTrans) {
		this.statusTrans = statusTrans;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getUntreadMmNo() {
		return untreadMmNo;
	}

	public void setUntreadMmNo(String untreadMmNo) {
		this.untreadMmNo = untreadMmNo;
	}

	public int getDtlCount() {
		return dtlCount;
	}

	public void setDtlCount(int dtlCount) {
		this.dtlCount = dtlCount;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	public String getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getItemTypeStr() {
		return itemTypeStr;
	}

	public void setItemTypeStr(String itemTypeStr) {
		this.itemTypeStr = itemTypeStr;
	}

	public String getQualityStr() {
		return qualityStr;
	}

	public void setQualityStr(String qualityStr) {
		this.qualityStr = qualityStr;
	}

	public String getConvertType() {
		return convertType;
	}

	public void setConvertType(String convertType) {
		this.convertType = convertType;
	}

	public String getConvertTypeStr() {
		return convertTypeStr;
	}

	public void setConvertTypeStr(String convertTypeStr) {
		this.convertTypeStr = convertTypeStr;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckFlagStatus() {
		return checkFlagStatus;
	}

	public void setCheckFlagStatus(String checkFlagStatus) {
		this.checkFlagStatus = checkFlagStatus;
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

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
}