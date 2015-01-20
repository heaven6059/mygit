/*
 * 类名 com.yougou.logistics.city.common.model.OsDefline
 * @author luo.hl
 * @date  Wed Sep 25 14:33:44 CST 2013
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

public class OsDefline extends OsDeflineKey {
	private String deliverType;

	private String transportType;

	private String lineName;

	private String lineFname;

	private String lineRemark;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineFname() {
		return lineFname;
	}

	public void setLineFname(String lineFname) {
		this.lineFname = lineFname;
	}

	public String getLineRemark() {
		return lineRemark;
	}

	public void setLineRemark(String lineRemark) {
		this.lineRemark = lineRemark;
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