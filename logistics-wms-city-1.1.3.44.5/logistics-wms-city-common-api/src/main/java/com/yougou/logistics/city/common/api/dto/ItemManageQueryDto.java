package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ItemManageQueryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 仓别
	 */
	private List<String> locnoList;
	/**
	 * 货号(商品编码从第三位起)
	 */
	private String productNo;
	/**
	 * 品牌编码
	 */
	private String brandNo;
	/**
	 * 一级大类编码
	 */
	private List<String> majorNoList;
	/**
	 * 年份编码
	 */
	private List<String> yearsList;
	/**
	 * 季节编码
	 */
	private List<String> seasonList;
	/**
	 * 性别编码
	 */
	private List<String> genderList;
	/**
	 * 页码
	 */
	private int pageNo;
	/**
	 * 每页数据量
	 */
	private int pageSize;
	/**
	 * 其它参数-用于扩展
	 */
	private Map<String, Object> otherParams;
	public List<String> getLocnoList() {
		return locnoList;
	}
	public void setLocnoList(List<String> locnoList) {
		this.locnoList = locnoList;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public List<String> getMajorNoList() {
		return majorNoList;
	}
	public void setMajorNoList(List<String> majorNoList) {
		this.majorNoList = majorNoList;
	}
	public List<String> getYearsList() {
		return yearsList;
	}
	public void setYearsList(List<String> yearsList) {
		this.yearsList = yearsList;
	}
	public List<String> getSeasonList() {
		return seasonList;
	}
	public void setSeasonList(List<String> seasonList) {
		this.seasonList = seasonList;
	}
	public List<String> getGenderList() {
		return genderList;
	}
	public void setGenderList(List<String> genderList) {
		this.genderList = genderList;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Map<String, Object> getOtherParams() {
		return otherParams;
	}
	public void setOtherParams(Map<String, Object> otherParams) {
		this.otherParams = otherParams;
	}
	
}
