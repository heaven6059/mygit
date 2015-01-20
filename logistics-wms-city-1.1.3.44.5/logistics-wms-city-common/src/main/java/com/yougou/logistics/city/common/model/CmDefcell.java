package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 储位
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:50:39
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefcell extends CmDefcellKey {
	/**
	 * 委托业主编码
	 */
	private String ownerNo;

	private String ownerName;

	/**
	 * 仓区编码
	 */
	private String wareNo;

	/**
	 * 仓区名称
	 */
	private String wareName;

	/**
	 * 储区编码
	 */
	private String areaNo;

	/**
	 * 储区名称
	 */
	private String areaName;

	/**
	 * 通道编码
	 */
	private String stockNo;

	/**
	 * 储格列
	 */
	private String stockX;

	/**
	 * 储格位
	 */
	private String bayX;

	/**
	 * 储格层
	 */
	private String stockY;

	/**
	 * 混载标志
	 */
	private Short mixFlag;

	/**
	 * 供应商混载标志
	 */
	private String mixSupplier;

	/**
	 * 最大板数
	 */
	private Short maxQty;

	/**
	 * 最大重量
	 */
	private BigDecimal maxWeight;

	/**
	 * 最大材积
	 */
	private BigDecimal maxVolume;

	/**
	 * 最大箱数
	 */
	private BigDecimal maxCase;

	/**
	 * 限制入库类型
	 */
	private String limitType;

	/**
	 * 限制比率
	 */
	private Short limitRate;

	/**
	 * 是否试算物流箱
	 */
	private String bPick;

	/**
	 * 储位状态
	 */
	private String cellStatus;

	private String showCellStatus;

	private String sourceCellStatus;

	/**
	 * 盘点状态
	 */
	private String checkStatus;

	private String showCheckStatus;

	/**
	 * 是否A类储区
	 */
	private String aFlag;

	/**
	 * 拣货标示
	 */
	private String pickFlag;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	/**
	 * 储区品质    0-9为良品，A-Z为不良品
	 */
	private String areaQuality;

	private String showAreaQuality;

	/**
	 * 商品类型
	 */
	private String itemType;

	private String showItemType;

	private String cellNoFuzzy;

	/**
	 * 储区属性   '0：作业区；1：暂存区；2;已配送区；3:问题区；4：虚拟区
	 */
	private String areaAttribute;

	/**
	 * 属性类型  '0：存储区1:进货；2:出货整理；3:出货复核；4:出货滑道；5:发货；6:退货；7:报损；8：直通；9：电子标签'
	 */
	private String attributeType;

	private String areaUsetype;
	/**
	 * 创建人中文名次
	 */
	private String creatorName;
	/**
	 * 修改人中文名称
	 */
	private String editorName;
	/**
	 * 长
	 */
	private BigDecimal length;
	/**
	 * 宽
	 */
	private BigDecimal width;
	/**
	 * 高
	 */
	private BigDecimal height;
	/**
	 * 容积
	 */
	private BigDecimal volume;
	
	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getWareNo() {
		return wareNo;
	}

	public void setWareNo(String wareNo) {
		this.wareNo = wareNo;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getStockX() {
		return stockX;
	}

	public void setStockX(String stockX) {
		this.stockX = stockX;
	}

	public String getBayX() {
		return bayX;
	}

	public void setBayX(String bayX) {
		this.bayX = bayX;
	}

	public String getStockY() {
		return stockY;
	}

	public void setStockY(String stockY) {
		this.stockY = stockY;
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

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}

	public BigDecimal getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(BigDecimal maxVolume) {
		this.maxVolume = maxVolume;
	}

	public BigDecimal getMaxCase() {
		return maxCase;
	}

	public void setMaxCase(BigDecimal maxCase) {
		this.maxCase = maxCase;
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

	public String getbPick() {
		return bPick;
	}

	public void setbPick(String bPick) {
		this.bPick = bPick;
	}

	public String getCellStatus() {
		return cellStatus;
	}

	public void setCellStatus(String cellStatus) {
		this.cellStatus = cellStatus;
		this.showCellStatus = SystemCache.getLookUpName("CELL_STATUS", cellStatus);
	}

	public String getSourceCellStatus() {
		return sourceCellStatus;
	}

	public void setSourceCellStatus(String sourceCellStatus) {
		this.sourceCellStatus = sourceCellStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
		this.showCheckStatus = SystemCache.getLookUpName("CHECK_STATUS", checkStatus);
	}

	public String getaFlag() {
		return aFlag;
	}

	public void setaFlag(String aFlag) {
		this.aFlag = aFlag;
	}

	public String getPickFlag() {
		return pickFlag;
	}

	public void setPickFlag(String pickFlag) {
		this.pickFlag = pickFlag;
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

	public String getAreaQuality() {
		return areaQuality;
	}

	public void setAreaQuality(String areaQuality) {
		this.areaQuality = areaQuality;
		this.showAreaQuality = SystemCache.getLookUpName("AREA_QUALITY", areaQuality);
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
		this.showItemType = SystemCache.getLookUpName("ITEM_TYPE", itemType);
	}

	public String getCellNoFuzzy() {
		return cellNoFuzzy;
	}

	public void setCellNoFuzzy(String cellNoFuzzy) {
		this.cellNoFuzzy = cellNoFuzzy;
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

	public String getAreaUsetype() {
		return areaUsetype;
	}

	public void setAreaUsetype(String areaUsetype) {
		this.areaUsetype = areaUsetype;
	}

	public String getShowCellStatus() {
		return showCellStatus;
	}

	public void setShowCellStatus(String showCellStatus) {
		this.showCellStatus = showCellStatus;
	}

	public String getShowCheckStatus() {
		return showCheckStatus;
	}

	public void setShowCheckStatus(String showCheckStatus) {
		this.showCheckStatus = showCheckStatus;
	}

	public String getShowAreaQuality() {
		return showAreaQuality;
	}

	public void setShowAreaQuality(String showAreaQuality) {
		this.showAreaQuality = showAreaQuality;
	}

	public String getShowItemType() {
		return showItemType;
	}

	public void setShowItemType(String showItemType) {
		this.showItemType = showItemType;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

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

}