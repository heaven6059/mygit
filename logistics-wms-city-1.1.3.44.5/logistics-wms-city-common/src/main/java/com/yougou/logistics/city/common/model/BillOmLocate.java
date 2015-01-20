/*
 * 类名 com.yougou.logistics.city.common.model.BillOmLocate
 * @author su.yq
 * @date  Mon Nov 04 14:35:57 CST 2013
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

public class BillOmLocate extends BillOmLocateKey {

	private String expType;

	private String status;

	private String locateName;

	private Date locateDate;

	private String fastFlag;

	private String divideFlag;

	private String specifyCell;

	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date expDate;

	private String hmManualFlag;

	private Long taskBatch;

	private String creator;
	
	private String creatorname;

	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	
	private String editorname;

	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date edittm;
	
	
	/**
	 * 用于页面展示
	 * @return
	 */
	
	private String ownerNo;//委托业主
	
	private String expNo;//出货订单号 
	
	private Date startCreatetm;// 起始创建日期

	private Date endCreatetm;// 结束创建日期
	
	private String statusStr;//状态

	private int totalItemQty;//总品项数

	private int totalLocateQty;//总数量

	private BigDecimal totalVolumeQty;//总体积数

	private BigDecimal totalWeightQty;//总重量
	
	private BigDecimal totalPlanQty;//总数量
	
	private String isContinue;//是否续调查询
	
	private String brandNo;//品牌编码
	
	private String sysNo;
	
	private String startCreatetmStr;
	private String endCreatetmStr;
	
	private String poNo;
	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}
	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocateName() {
		return locateName;
	}

	public void setLocateName(String locateName) {
		this.locateName = locateName;
	}

	public Date getLocateDate() {
		return locateDate;
	}

	public void setLocateDate(Date locateDate) {
		this.locateDate = locateDate;
	}

	public String getFastFlag() {
		return fastFlag;
	}

	public void setFastFlag(String fastFlag) {
		this.fastFlag = fastFlag;
	}

	public String getDivideFlag() {
		return divideFlag;
	}

	public void setDivideFlag(String divideFlag) {
		this.divideFlag = divideFlag;
	}

	public String getSpecifyCell() {
		return specifyCell;
	}

	public void setSpecifyCell(String specifyCell) {
		this.specifyCell = specifyCell;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getHmManualFlag() {
		return hmManualFlag;
	}

	public void setHmManualFlag(String hmManualFlag) {
		this.hmManualFlag = hmManualFlag;
	}

	public Long getTaskBatch() {
		return taskBatch;
	}

	public void setTaskBatch(Long taskBatch) {
		this.taskBatch = taskBatch;
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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public int getTotalItemQty() {
		return totalItemQty;
	}

	public void setTotalItemQty(int totalItemQty) {
		this.totalItemQty = totalItemQty;
	}

	public int getTotalLocateQty() {
		return totalLocateQty;
	}

	public void setTotalLocateQty(int totalLocateQty) {
		this.totalLocateQty = totalLocateQty;
	}

	public BigDecimal getTotalVolumeQty() {
		return totalVolumeQty;
	}

	public void setTotalVolumeQty(BigDecimal totalVolumeQty) {
		this.totalVolumeQty = totalVolumeQty;
	}

	public BigDecimal getTotalWeightQty() {
		return totalWeightQty;
	}

	public void setTotalWeightQty(BigDecimal totalWeightQty) {
		this.totalWeightQty = totalWeightQty;
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

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getIsContinue() {
		return isContinue;
	}

	public void setIsContinue(String isContinue) {
		this.isContinue = isContinue;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public BigDecimal getTotalPlanQty() {
		return totalPlanQty;
	}

	public void setTotalPlanQty(BigDecimal totalPlanQty) {
		this.totalPlanQty = totalPlanQty;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getStartCreatetmStr() {
		return startCreatetmStr;
	}

	public void setStartCreatetmStr(String startCreatetmStr) {
		this.startCreatetmStr = startCreatetmStr;
	}

	public String getEndCreatetmStr() {
		return endCreatetmStr;
	}

	public void setEndCreatetmStr(String endCreatetmStr) {
		this.endCreatetmStr = endCreatetmStr;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}
	
}