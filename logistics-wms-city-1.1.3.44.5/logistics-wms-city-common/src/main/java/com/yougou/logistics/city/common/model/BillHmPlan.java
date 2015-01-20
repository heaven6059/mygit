/*
 * 类名 com.yougou.logistics.city.common.model.BillHmPlan
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillHmPlan extends BillHmPlanKey {

	private String ownerNo;

	private String sourceType;

	private String outstockType;

	private String status;
	
	private String updStatus;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date moveDate;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String auditor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	private String remark;

	private String ownerName;
	
	private String sourceNo;
	
	/**铺助属性**/

	private String statusStr;//状态显示

	private Date startCreatetm;// 起始创建日期

	private Date endCreatetm;// 结束创建日期

	private Date startAudittm;// 起始审核日期

	private Date endAudittm;// 结束审核日期
	
	private String businessType;

	//显示中文名称
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

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getOutstockType() {
		return outstockType;
	}

	public void setOutstockType(String outstockType) {
		this.outstockType = outstockType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getMoveDate() {
		return moveDate;
	}

	public void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

	public String getUpdStatus() {
		return updStatus;
	}

	public void setUpdStatus(String updStatus) {
		this.updStatus = updStatus;
	}
	
}