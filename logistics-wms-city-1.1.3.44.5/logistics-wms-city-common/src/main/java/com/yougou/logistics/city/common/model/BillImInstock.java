/*
 * 类名 com.yougou.logistics.city.common.model.BillImInstock
 * @author luo.hl
 * @date  Mon Sep 30 09:51:28 CST 2013
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

public class BillImInstock extends BillImInstockKey {
	private String ownerName;

	private String autoLocateFlag;

	private String status;

	private String dispatchWorker;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date dispatchDate;

	private String instockWorker;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date instockDate;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String operateType;

	private String locateType;

	private String containerLocateFlag;

	private String auditor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;
	
	/**
	 * 用于页面显示
	 */
	private String statusStr;//状态

	/**
	 * 中文名称
	 */
	private String creatorName;
	
	private String editorName;
	
	private String auditorName;
	
	private String instockName;
	
	private String dispatchName;
	
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

	public String getInstockName() {
		return instockName;
	}

	public void setInstockName(String instockName) {
		this.instockName = instockName;
	}

	public String getDispatchName() {
		return dispatchName;
	}

	public void setDispatchName(String dispatchName) {
		this.dispatchName = dispatchName;
	}

	public String getAutoLocateFlag() {
		return autoLocateFlag;
	}

	public void setAutoLocateFlag(String autoLocateFlag) {
		this.autoLocateFlag = autoLocateFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDispatchWorker() {
		return dispatchWorker;
	}

	public void setDispatchWorker(String dispatchWorker) {
		this.dispatchWorker = dispatchWorker;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getInstockWorker() {
		return instockWorker;
	}

	public void setInstockWorker(String instockWorker) {
		this.instockWorker = instockWorker;
	}

	public Date getInstockDate() {
		return instockDate;
	}

	public void setInstockDate(Date instockDate) {
		this.instockDate = instockDate;
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

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getLocateType() {
		return locateType;
	}

	public void setLocateType(String locateType) {
		this.locateType = locateType;
	}

	public String getContainerLocateFlag() {
		return containerLocateFlag;
	}

	public void setContainerLocateFlag(String containerLocateFlag) {
		this.containerLocateFlag = containerLocateFlag;
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

}