/*
 * 类名 com.yougou.logistics.city.common.model.BillConStorelock
 * @author yougoupublic
 * @date  Sat Mar 08 11:25:53 CST 2014
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
import com.yougou.logistics.city.common.enums.StorelockTypeEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillConStorelock extends BillConStorelockKey {

	private String storeNo;

	private String storelockType;

	private String status;

	private String updStatus;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String auditor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	private String sourceType;

	private String sourceNo;

	private String businessType;

	/** 用户页面显示 **/

	private String storeName;

	private String ownerName;

	private String statusStr;

	private String brandNo;

	private Date startCreatetm;

	private Date endCreatetm;

	private Date startAudittm;

	private Date endAudittm;

	private String sourceTypeStr;

	private String storelockTypeStr;

	private String businessTypeStr;

	/**
	 * 实际数量
	 */
	private BigDecimal itemQty;
	private String creatorName;
	private String editorName;
	private String auditorName;

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

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStorelockType() {
		return storelockType;
	}

	public void setStorelockType(String storelockType) {
		this.storelockType = storelockType;
		this.storelockTypeStr = StorelockTypeEnums
				.getTextByStatus(this.storelockType);
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

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Date getStartCreatetm() {
		return startCreatetm;
	}

	public void setStartCreatetm(Date startCreatetm) {
		this.startCreatetm = startCreatetm;
	}

	public Date getEndCreatetm() {
		return endCreatetm;
	}

	public void setEndCreatetm(Date endCreatetm) {
		this.endCreatetm = endCreatetm;
	}

	public Date getStartAudittm() {
		return startAudittm;
	}

	public void setStartAudittm(Date startAudittm) {
		this.startAudittm = startAudittm;
	}

	public Date getEndAudittm() {
		return endAudittm;
	}

	public void setEndAudittm(Date endAudittm) {
		this.endAudittm = endAudittm;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceTypeStr() {
		return sourceTypeStr;
	}

	public void setSourceTypeStr(String sourceTypeStr) {
		this.sourceTypeStr = sourceTypeStr;
	}

	public String getStorelockTypeStr() {
		return storelockTypeStr;
	}

	public void setStorelockTypeStr(String storelockTypeStr) {
		this.storelockTypeStr = storelockTypeStr;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessTypeStr() {
		return businessTypeStr;
	}

	public void setBusinessTypeStr(String businessTypeStr) {
		this.businessTypeStr = businessTypeStr;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public String getUpdStatus() {
		return updStatus;
	}

	public void setUpdStatus(String updStatus) {
		this.updStatus = updStatus;
	}

}