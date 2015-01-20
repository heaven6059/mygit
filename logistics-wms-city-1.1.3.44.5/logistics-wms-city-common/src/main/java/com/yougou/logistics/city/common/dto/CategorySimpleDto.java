package com.yougou.logistics.city.common.dto;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-18 下午4:48:40
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class CategorySimpleDto {
	private String cateCode;
    private String cateName;
    private String searchCode;
	public String getCateCode() {
		return cateCode;
	}
	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
    
}
