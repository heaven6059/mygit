package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-7-31 下午4:28:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BizBillDetailsReport {
	private Date createtm;//作业日期
	private String brandName;//品牌名称
	private String billNo;//单号
	private String billTypeName;//单据类型名称
	private String statusName;//状态名称
	private String itemNo;//商品编码
	private String sizeNo;//商品尺码
	private String boxNo;//箱号
	private BigDecimal planQty;//计划数
	private BigDecimal realQty;//实数
	private String sourceBillNo;//来源单据
	private String sourceBillTypeName;//来源单据类型名称
	private String editor;//更新人
	private String editorName;//更新人
	private Date edittm;//更新时间
	public Date getCreatetm() {
		return createtm;
	}
	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getSizeNo() {
		return sizeNo;
	}
	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}
	public String getBoxNo() {
		return boxNo;
	}
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	public BigDecimal getPlanQty() {
		return planQty;
	}
	public void setPlanQty(BigDecimal planQty) {
		this.planQty = planQty;
	}
	public BigDecimal getRealQty() {
		return realQty;
	}
	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}
	public String getSourceBillNo() {
		return sourceBillNo;
	}
	public void setSourceBillNo(String sourceBillNo) {
		this.sourceBillNo = sourceBillNo;
	}
	public String getSourceBillTypeName() {
		return sourceBillTypeName;
	}
	public void setSourceBillTypeName(String sourceBillTypeName) {
		this.sourceBillTypeName = sourceBillTypeName;
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
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	
}
