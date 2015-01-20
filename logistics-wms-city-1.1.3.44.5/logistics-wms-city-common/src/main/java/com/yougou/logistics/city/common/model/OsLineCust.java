/*
 * 类名 com.yougou.logistics.city.common.model.OsLineCust
 * @author luo.hl
 * @date  Thu Sep 26 12:33:06 CST 2013
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

public class OsLineCust extends OsLineCustKey {
	private String storeName;

	private BigDecimal lineSeqNo;

	private BigDecimal distance;

	private BigDecimal charge;

	private String tollNoArray;

	private BigDecimal speedLimit;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String midFlag;

	public BigDecimal getLineSeqNo() {
		return lineSeqNo;
	}

	public void setLineSeqNo(BigDecimal lineSeqNo) {
		this.lineSeqNo = lineSeqNo;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public String getTollNoArray() {
		return tollNoArray;
	}

	public void setTollNoArray(String tollNoArray) {
		this.tollNoArray = tollNoArray;
	}

	public BigDecimal getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(BigDecimal speedLimit) {
		this.speedLimit = speedLimit;
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

	public String getMidFlag() {
		return midFlag;
	}

	public void setMidFlag(String midFlag) {
		this.midFlag = midFlag;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

}