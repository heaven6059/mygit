/*
 * 类名 com.yougou.logistics.city.common.model.BillWmOutstock
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillWmOutstock extends BillWmOutstockKey {
	private Date operateDate;

	private String operateType;

	private String status;

	private String creator;
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date createtm;

	private String editor;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String auditor;

	private Date audittm;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date outstockDate;

	private String outstockName;
	
	private String assignName;
	
	private String sysNo;
	
	private String brandNo;

	private String creatorName;
	private String editorName;
	private String auditorName;
	private String assignChName;
    private String outstockChName;
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
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

	public Date getOutstockDate() {
		return outstockDate;
	}

	public void setOutstockDate(Date outstockDate) {
		this.outstockDate = outstockDate;
	}

	public String getOutstockName() {
		return outstockName;
	}

	public void setOutstockName(String outstockName) {
		this.outstockName = outstockName;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
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

	public String getAssignChName() {
		return assignChName;
	}

	public void setAssignChName(String assignChName) {
		this.assignChName = assignChName;
	}

	public String getOutstockChName() {
		return outstockChName;
	}

	public void setOutstockChName(String outstockChName) {
		this.outstockChName = outstockChName;
	}
	
}