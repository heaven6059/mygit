package com.yougou.logistics.city.common.model;

/**
 /**
 * 导入储位
 * @author wanghb
 * @date 2014-10-11 上午11:22:58
 * @version 1.1.3.41
 * @copyright yougou.com
 */
public class TmpCmDefCellExcel {

	private String locNo;
	private String wareNo;//仓区编码
	private String wareName;//仓区名称
	private String areaNo;//库区编码
	private String areaName;//库区名称
	private String areaType;//库存类型
	private String areaAttribute;//库区属性
	private String areaUsetype;//库存用途
	private String stockNo;//通道编码
	private String stockName;//通道名称
	private String stockX;//储格列	
	private String stockY;//储格层	
	private String bayX;//储格位
	private String cellNo;//储位编码 
	private String itemType;//商品类型 
	private String quality;//商品品质
	private String mixFlag;//混载标志
	private String uuId;
	
	private String itemTypeValue;
	private String qualityValue;
	private String mixFlagValue;
	
	public String getUuId() {
		return uuId;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
	public String getLocNo() {
		return locNo;
	}
	public void setLocNo(String locNo) {
		this.locNo = locNo;
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
	public String getAreaType() {
		return areaType;
	}
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	public String getAreaAttribute() {
		return areaAttribute;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public void setAreaAttribute(String areaAttribute) {
		this.areaAttribute = areaAttribute;
	}
	public String getAreaUsetype() {
		return areaUsetype;
	}
	public void setAreaUsetype(String areaUsetype) {
		this.areaUsetype = areaUsetype;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockX() {
		return stockX;
	}
	public void setStockX(String stockX) {
		this.stockX = stockX;
	}
	public String getStockY() {
		return stockY;
	}
	public void setStockY(String stockY) {
		this.stockY = stockY;
	}
	public String getBayX() {
		return bayX;
	}
	public void setBayX(String bayX) {
		this.bayX = bayX;
	}
	public String getCellNo() {
		return cellNo;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getMixFlag() {
		return mixFlag;
	}
	public void setMixFlag(String mixFlag) {
		this.mixFlag = mixFlag;
	}
	public String getItemTypeValue() {
		return itemTypeValue;
	}
	public void setItemTypeValue(String itemTypeValue) {
		this.itemTypeValue = itemTypeValue;
	}
	public String getQualityValue() {
		return qualityValue;
	}
	public void setQualityValue(String qualityValue) {
		this.qualityValue = qualityValue;
	}
	public String getMixFlagValue() {
		return mixFlagValue;
	}
	public void setMixFlagValue(String mixFlagValue) {
		this.mixFlagValue = mixFlagValue;
	}
}
