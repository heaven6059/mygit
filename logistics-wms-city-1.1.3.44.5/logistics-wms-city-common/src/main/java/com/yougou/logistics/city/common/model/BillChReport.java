package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 盘点报表
 * @author JYS
 *
 */
public class BillChReport {
	private String planNo;
	private String planType;
	private String planStatus;
	private String checkNo;
	private String brandNo;
	private String brandName;
	private String wareNo;
	private String areaNo;
	private String areaName;
	private String stockNo;
	private String stockName;
	private String cellNo;
	private String cateNo;
	private String cateName;
	private String years;
	private String yearsName;
	private String season;
	private String seasonName;
	private String gender;
	private String genderName;
	private String itemNo;
	private String sizeNo;
	private String barcode;
	private BigDecimal itemQty;
	private BigDecimal realQty;
	private BigDecimal diffQty;
	
	public BigDecimal getDiffQty() {
		return diffQty;
	}
	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
	}
	public String getPlanNo() {
		return planNo;
	}
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
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
	public String getCellNo() {
		return cellNo;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
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
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getYearsName() {
		this.yearsName = SystemCache.getLookUpName("YEARS", years);
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
		this.seasonName = SystemCache.getLookUpName("SEASON", season);
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
		this.genderName = SystemCache.getLookUpName("GENDER", gender);
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	
}
