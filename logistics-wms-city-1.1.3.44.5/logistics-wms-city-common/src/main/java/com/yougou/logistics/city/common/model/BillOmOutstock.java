/*
 * 类名 com.yougou.logistics.city.common.model.BillOmOutstock
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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
import com.yougou.logistics.city.common.enums.BillOmOutstockOperateEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillOmOutstock extends BillOmOutstockKey {
	private Date operateDate;

	private String outstockType;

	private String batchNo;

	private String operateType;

	private String pickType;

	private String taskType;

	private String status;
	
	private String updStatus;

	private Short priority;

	private String dockNo;

	private Date handinDate;

	private Date handoutDate;

	private String handinName;

	private String handoutName;

	private String printStatus;

	private String creator;
	
	private String creatorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	
	private String editorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private Date expDate;

	private String labelpickFlag;

	private String massFlag;

	private String auditor;
	
	private String auditorname;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date outstockDate;

	private String outstockName;

	private String instockName;

	private String assignName;
	
	private String assignNameCh;
	
	private String outstockSendType;
	
	private String sourceNo;
	
	private String sourceType;
	
	private String remark;
	
	/**
	 * 铺助属性
	 * @return
	 */

	private String statusStr;//状态 用于页面展示

	private Date createTmStart;// 起始创建日期

	private Date createTmEnd;// 结束创建日期

	private Date startAudittm;// 起始审核日期

	private Date endAudittm;// 结束审核日期
	
	private String outstockSendTypeStr;//拣货类型

	private String locateNo;
	
	private String brandNo;
	
	private BigDecimal itemQty;
	
	private String storeName;
	
	private BigDecimal realQty;//总复核数量
	
	private BigDecimal outstockedQty;
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOutstockType() {
		return outstockType;
	}

	public void setOutstockType(String outstockType) {
		this.outstockType = outstockType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getPickType() {
		return pickType;
	}

	public void setPickType(String pickType) {
		this.pickType = pickType;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Short getPriority() {
		return priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public String getDockNo() {
		return dockNo;
	}

	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}

	public Date getHandinDate() {
		return handinDate;
	}

	public void setHandinDate(Date handinDate) {
		this.handinDate = handinDate;
	}

	public Date getHandoutDate() {
		return handoutDate;
	}

	public void setHandoutDate(Date handoutDate) {
		this.handoutDate = handoutDate;
	}

	public String getHandinName() {
		return handinName;
	}

	public void setHandinName(String handinName) {
		this.handinName = handinName;
	}

	public String getHandoutName() {
		return handoutName;
	}

	public void setHandoutName(String handoutName) {
		this.handoutName = handoutName;
	}

	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
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

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getLabelpickFlag() {
		return labelpickFlag;
	}

	public void setLabelpickFlag(String labelpickFlag) {
		this.labelpickFlag = labelpickFlag;
	}

	public String getMassFlag() {
		return massFlag;
	}

	public void setMassFlag(String massFlag) {
		this.massFlag = massFlag;
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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public Date getCreateTmStart() {
		return createTmStart;
	}

	public void setCreateTmStart(Date createTmStart) {
		this.createTmStart = createTmStart;
	}

	public Date getCreateTmEnd() {
		return createTmEnd;
	}

	public void setCreateTmEnd(Date createTmEnd) {
		this.createTmEnd = createTmEnd;
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

	public String getInstockName() {
		return instockName;
	}

	public void setInstockName(String instockName) {
		this.instockName = instockName;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getLocateNo() {
		return locateNo;
	}

	public void setLocateNo(String locateNo) {
		this.locateNo = locateNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getOutstockSendType() {
		return outstockSendType;
	}

	public void setOutstockSendType(String outstockSendType) {
		this.outstockSendType = outstockSendType;
		this.outstockSendTypeStr = BillOmOutstockOperateEnums.getTextByStatus(this.outstockSendType);
	}

	public String getOutstockSendTypeStr() {
		return outstockSendTypeStr;
	}

	public void setOutstockSendTypeStr(String outstockSendTypeStr) {
		this.outstockSendTypeStr = outstockSendTypeStr;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public BigDecimal getOutstockedQty() {
		return outstockedQty;
	}

	public void setOutstockedQty(BigDecimal outstockedQty) {
		this.outstockedQty = outstockedQty;
	}

	public String getUpdStatus() {
		return updStatus;
	}

	public void setUpdStatus(String updStatus) {
		this.updStatus = updStatus;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getAuditorname() {
		return auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}

	public String getAssignNameCh() {
		return assignNameCh;
	}

	public void setAssignNameCh(String assignNameCh) {
		this.assignNameCh = assignNameCh;
	}
	
}