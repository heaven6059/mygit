package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 商品汇总库存
 * 
 * @author jiang.ys
 * @date 2013-12-3 上午10:26:53
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class ItemCollectContent {
	private String locno;
	private String brandNo;
	private String brandName;
	private String cellNo;
	private String itemNo;
	private String itemName;
	private String wareNo;
	private String areaNo;
	private String areaName;
	private String cateNo;//商品类别
	private String cateName;//商品类别
	private String itemType;//商品属性
	private String itemTypeName;//商品属性
	private String quality;
	private String qualityName;
	private String barcode;
	private String colorNo;
	private String colorName;
	private String sizeNo;
	private BigDecimal qty;
	private BigDecimal outstockQty;//预下数量
	private BigDecimal instockQty;//预上数量
	private BigDecimal unusualQty;//例外数量
	private BigDecimal usableQty;//可用数量
	
	private String status;
	private String statusName;
	private String hmManualFlag;
	private String manualName;
	private String years;
	private String yearsName;
	private String season;
	private String seasonName;
	private String gender;
	private String genderName;
	
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getYearsName() {
		return yearsName;
	}
	public void setYearsName(String yearsName) {
		this.yearsName = yearsName;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getSeasonName() {
		return seasonName;
	}
	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getHmManualFlag() {
		return hmManualFlag;
	}
	public void setHmManualFlag(String hmManualFlag) {
		this.hmManualFlag = hmManualFlag;
	}
	public String getManualName() {
		return manualName;
	}
	public void setManualName(String manualName) {
		this.manualName = manualName;
	}
	public String getLocno() {
		return locno;
	}
	public void setLocno(String locno) {
		this.locno = locno;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCellNo() {
		return cellNo;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getWareNo() {
		return wareNo;
	}
	public void setWareNo(String wareNo) {
		this.wareNo = wareNo;
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
	public String getCateNo() {
		return cateNo;
	}
	public void setCateNo(String cateNo) {
		this.cateNo = cateNo;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemTypeName() {
		return itemTypeName;
	}
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
		this.qualityName = SystemCache.getLookUpName("AREA_QUALITY", this.quality);
	}
	public String getQualityName() {
		return qualityName;
	}
	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getColorNo() {
		return colorNo;
	}
	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSizeNo() {
		return sizeNo;
	}
	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getOutstockQty() {
		return outstockQty;
	}
	public void setOutstockQty(BigDecimal outstockQty) {
		this.outstockQty = outstockQty;
	}
	public BigDecimal getInstockQty() {
		return instockQty;
	}
	public void setInstockQty(BigDecimal instockQty) {
		this.instockQty = instockQty;
	}
	public BigDecimal getUnusualQty() {
		return unusualQty;
	}
	public void setUnusualQty(BigDecimal unusualQty) {
		this.unusualQty = unusualQty;
	}
	public BigDecimal getUsableQty() {
		return usableQty;
	}
	public void setUsableQty(BigDecimal usableQty) {
		this.usableQty = usableQty;
	}
	
	
}
