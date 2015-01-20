/*
 * 类名 com.yougou.logistics.city.common.model.BmDefprinterGroup
 * @author luo.hl
 * @date  Fri Nov 01 11:21:40 CST 2013
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

public class BmDefprinterGroup extends BmDefprinterGroupKey {
	private String printerGroupName;

	private Short status;

	private String creator;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	public String getPrinterGroupName() {
		return printerGroupName;
	}

	public void setPrinterGroupName(String printerGroupName) {
		this.printerGroupName = printerGroupName;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
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
}