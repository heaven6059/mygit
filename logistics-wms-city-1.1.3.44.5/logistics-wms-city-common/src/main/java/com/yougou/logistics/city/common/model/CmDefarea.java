/*
 * 类名 com.yougou.logistics.city.common.model.CmDefarea
 * @author qin.dy
 * @date  Wed Sep 25 16:42:38 CST 2013
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

/**
 * 	库区
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:49:53
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefarea extends CmDefareaKey {
	/**
	 * 储区名称
	 */
	private String areaName;

	/**
	 * 储区备注
	 */
	private String areaRemark;

	/**
	 * 下架方式    P-栈板；C-整箱；B-零散
	 */
	private String oType;

	/**
	 * 货架类型
	 */
	private String areaType;

	/**
	 * 储区用途   1:普通存储区；2：报损区；3：退货区；5：异常区；6:贵重品区'
	 */
	private String areaUsetype;

	/**
	 * 储区品质    0-9为良品，A-Z为不良品
	 */
	private String areaQuality;

	/**
	 * 混载标志   '0:不可混；1：同商品不同属性混；2：不同商品混'
	 */
	private Short mixFlag;

	/**
	 * 供应商混载标志  '退货时是否混供应商.0:不混供应商；1：混供应商'
	 */
	private String mixSupplier;

	/**
	 * 最大板数
	 */
	private Short maxQty;

	/**
	 * 走道数量
	 */
	private Short stockNum;

	/**
	 * 播种标示   0：摘果；1：分播；
	 */
	private String divideFlag;

	/**
	 * 播种标示   '0：物流箱摘果；1：物流箱分播；2：批次别物流箱拣货
	 */
	private String bDivideFlag;

	/**
	 * 储区属性   '0：作业区；1：暂存区；2;已配送区；3:问题区；4：虚拟区
	 */
	private String areaAttribute;

	/**
	 * 属性类型  '0：存储区1:进货；2:出货整理；3:出货复核；4:出货滑道；5:发货；6:退货；7:报损；8：直通；9：电子标签'
	 */
	private String attributeType;

	/**
	 * 限制入库类型    0->标准堆叠比率；1->件数或箱数；默认为0
	 */
	private String limitType;

	/**
	 * 限制比率  0：表示不限制，100：表示必须满板
	 */
	private Short limitRate;

	/**
	 * 板型出货比率    填写0~100的比率；若类型为1，填写件数或箱数数字
	 */
	private Short palOutRate;

	/**
	 * 是否试算物流箱   0：不做物流箱试算；1：做虚拟物流箱试算；2：按实际物流箱做试算
	 */
	private String bPick;

	/**
	 * 是否拣货区   0：非拣货区；1：拣货区
	 */
	private String areaPick;

	/**
	 * 是否A类储区  0：非A类储区；1：A类储区
	 */
	private String aFlag;

	/**
	 * 是否使用上下架暂存区   0：不使用；1：使用
	 */
	private String ioBufferFlag;

	/**
	 * 是否允许拣货  0-不允许拣货；1--允许拣货
	 */
	private String pickFlag;

	/**
	 * 楼层  
	 */
	private String floor;

	/**
	 * 是否需要做提前拣选设置   0-无提前拣选；1-有提前拣选'
	 */
	private String advancerPickFlag;

	private String creator;

	@JsonSerialize(using=JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using=JsonDateSerializer$19.class)
	private Date edittm;

	/**
	 * 最大箱数  
	 */
	private BigDecimal maxCase;

	/**
	 * 商品类型
	 */
	private String itemType;

	/**附加属性**/
	private String wareAreaNo;//仓区编码+储区编码
	
	private String wareName;
	
	/**
	 * 创建人中文名次
	 */
	private String creatorName;
	/**
	 * 修改人中文名称
	 */
	private String editorName;
	

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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaRemark() {
		return areaRemark;
	}

	public void setAreaRemark(String areaRemark) {
		this.areaRemark = areaRemark;
	}

	public String getoType() {
		return oType;
	}

	public void setoType(String oType) {
		this.oType = oType;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getAreaUsetype() {
		return areaUsetype;
	}

	public void setAreaUsetype(String areaUsetype) {
		this.areaUsetype = areaUsetype;
	}

	public String getAreaQuality() {
		return areaQuality;
	}

	public void setAreaQuality(String areaQuality) {
		this.areaQuality = areaQuality;
	}

	public Short getMixFlag() {
		return mixFlag;
	}

	public void setMixFlag(Short mixFlag) {
		this.mixFlag = mixFlag;
	}

	public String getMixSupplier() {
		return mixSupplier;
	}

	public void setMixSupplier(String mixSupplier) {
		this.mixSupplier = mixSupplier;
	}

	public Short getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Short maxQty) {
		this.maxQty = maxQty;
	}

	public Short getStockNum() {
		return stockNum;
	}

	public void setStockNum(Short stockNum) {
		this.stockNum = stockNum;
	}

	public String getDivideFlag() {
		return divideFlag;
	}

	public void setDivideFlag(String divideFlag) {
		this.divideFlag = divideFlag;
	}

	public String getbDivideFlag() {
		return bDivideFlag;
	}

	public void setbDivideFlag(String bDivideFlag) {
		this.bDivideFlag = bDivideFlag;
	}

	public String getAreaAttribute() {
		return areaAttribute;
	}

	public void setAreaAttribute(String areaAttribute) {
		this.areaAttribute = areaAttribute;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public Short getLimitRate() {
		return limitRate;
	}

	public void setLimitRate(Short limitRate) {
		this.limitRate = limitRate;
	}

	public Short getPalOutRate() {
		return palOutRate;
	}

	public void setPalOutRate(Short palOutRate) {
		this.palOutRate = palOutRate;
	}

	public String getbPick() {
		return bPick;
	}

	public void setbPick(String bPick) {
		this.bPick = bPick;
	}

	public String getAreaPick() {
		return areaPick;
	}

	public void setAreaPick(String areaPick) {
		this.areaPick = areaPick;
	}

	public String getaFlag() {
		return aFlag;
	}

	public void setaFlag(String aFlag) {
		this.aFlag = aFlag;
	}

	public String getIoBufferFlag() {
		return ioBufferFlag;
	}

	public void setIoBufferFlag(String ioBufferFlag) {
		this.ioBufferFlag = ioBufferFlag;
	}

	public String getPickFlag() {
		return pickFlag;
	}

	public void setPickFlag(String pickFlag) {
		this.pickFlag = pickFlag;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getAdvancerPickFlag() {
		return advancerPickFlag;
	}

	public void setAdvancerPickFlag(String advancerPickFlag) {
		this.advancerPickFlag = advancerPickFlag;
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

	public BigDecimal getMaxCase() {
		return maxCase;
	}

	public void setMaxCase(BigDecimal maxCase) {
		this.maxCase = maxCase;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getWareAreaNo() {
		return wareAreaNo;
	}

	public void setWareAreaNo(String wareAreaNo) {
		this.wareAreaNo = wareAreaNo;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

}