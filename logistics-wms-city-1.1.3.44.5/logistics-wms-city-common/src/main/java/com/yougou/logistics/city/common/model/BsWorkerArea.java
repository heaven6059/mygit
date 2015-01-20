/*
 * 类名 com.yougou.logistics.city.common.model.BsWorkerArea
 * @author luo.hl
 * @date  Wed Sep 25 14:31:21 CST 2013
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

public class BsWorkerArea extends BsWorkerAreaKey {

	private String workerName;

	private String aStockNo;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private Short workPriority;

	private String wareName;

	private String areaName;

	public String getaStockNo() {
		return aStockNo;
	}

	public void setaStockNo(String aStockNo) {
		this.aStockNo = aStockNo;
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

	public Short getWorkPriority() {
		return workPriority;
	}

	public void setWorkPriority(Short workPriority) {
		this.workPriority = workPriority;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}