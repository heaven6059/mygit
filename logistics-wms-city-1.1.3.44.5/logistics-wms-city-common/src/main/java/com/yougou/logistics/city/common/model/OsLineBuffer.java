/*
 * 类名 com.yougou.logistics.city.common.model.OsLineBuffer
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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

public class OsLineBuffer extends OsLineBufferKey {
	private String lineName;

	private String aStockNo;

	private String wareName;

	private String areaName;

	private String bufferName;

	private String status;

	private BigDecimal useVolumn;

	private BigDecimal useWeight;

	private BigDecimal useBoxnum;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;
	
	/**
	 * 承运商编码
	 */
	private String supplierNo;
	

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getaStockNo() {
		return aStockNo;
	}

	public void setaStockNo(String aStockNo) {
		this.aStockNo = aStockNo;
	}

	public String getBufferName() {
		return bufferName;
	}

	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getUseVolumn() {
		return useVolumn;
	}

	public void setUseVolumn(BigDecimal useVolumn) {
		this.useVolumn = useVolumn;
	}

	public BigDecimal getUseWeight() {
		return useWeight;
	}

	public void setUseWeight(BigDecimal useWeight) {
		this.useWeight = useWeight;
	}

	public BigDecimal getUseBoxnum() {
		return useBoxnum;
	}

	public void setUseBoxnum(BigDecimal useBoxnum) {
		this.useBoxnum = useBoxnum;
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

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
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