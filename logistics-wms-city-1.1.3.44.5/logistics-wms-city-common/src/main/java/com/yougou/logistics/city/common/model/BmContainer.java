package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 容器基础资料，迁移到BM_CONTAINER表
 * @author wanghb
 * @date 2014-7-30
 * @version 1.1.3.37
 */
public class BmContainer {
	private String conNo;//容器编码
	private String locno;//仓别
	private String ownerNo;//委托业主
	private String type;//容器类型(C=周转箱 P=托盘R=笼车)
	private String weight;//重量
	private BigDecimal volume;//体积
	private BigDecimal length;//长
	private BigDecimal wide;//宽
	private BigDecimal height;//高
	private String status;//容器状态(0=可用 1=占用)
	private String mixStyle;//是否混款(预留字段 0=不混款 1=混款)
	private String isDeleted;//是否逻辑删除(0=否 1=是)
	private String creator;//建档人
	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date createtm;//建档时间
	private String editor;//修改人
	@JsonSerialize(using =JsonDateSerializer$19.class)
	private Date edittm;//修改时间
	private String remarks;//备注
	private String optBillNo;//占用单号
	private String optBillType;//占用单据类型 A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板;HM-移库;CH-盘点;
	private String falg;//解锁容器标示，此值不为空会清空optBillNo、optBillType
	private String conStatus;//库存状态
	private String transFlag;
	
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}
	public String getLocno() {
		return locno;
	}
	public void setLocno(String locno) {
		this.locno = locno;
	}
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getLength() {
		return length;
	}
	public void setLength(BigDecimal length) {
		this.length = length;
	}
	public BigDecimal getWide() {
		return wide;
	}
	public void setWide(BigDecimal wide) {
		this.wide = wide;
	}
	public BigDecimal getHeight() {
		return height;
	}
	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMixStyle() {
		return mixStyle;
	}
	public void setMixStyle(String mixStyle) {
		this.mixStyle = mixStyle;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOptBillNo() {
		return optBillNo;
	}
	public void setOptBillNo(String optBillNo) {
		this.optBillNo = optBillNo;
	}
	public String getOptBillType() {
		return optBillType;
	}
	public void setOptBillType(String optBillType) {
		this.optBillType = optBillType;
	}
	public String getFalg() {
		return falg;
	}
	public void setFalg(String falg) {
		this.falg = falg;
	}
	public String getConStatus() {
		if(StringUtils.isBlank(conStatus)){
			return "-10";
		}
		return conStatus;
	}
	public void setConStatus(String conStatus) {
		this.conStatus = conStatus;
	}
	public String getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
}
