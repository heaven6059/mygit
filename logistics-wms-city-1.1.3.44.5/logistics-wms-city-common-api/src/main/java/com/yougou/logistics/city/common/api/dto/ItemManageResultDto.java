package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;


public class ItemManageResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 仓别
	 */
	private String locno;
	/**
	 * 货号(商品编码从第三位起)
	 */
	private String productNo;
	/**
	 * 商品编码
	 */
	private String itemNo;
	/**
	 * 商品名称
	 */
	private String itemName;
	/**
	 * 品质
	 */
	private String quality;
	/**
	 * 品质名称
	 */
	private String qualityName;
	/**
	 * 颜色编码
	 */
	private String colorNo;
	/**
	 * 颜色名称
	 */
	private String colorName;
	/**
	 * 商品类别
	 */
	private String cateNo;
	/**
	 * 商品类别名称
	 */
	private String cateName;
	/**
	 * 品牌编码
	 */
	private String brandNo;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 年份编码
	 */
	private String years;
	/**
	 * 年份名称
	 */
	private String yearsName;
	/**
	 * 季节编码
	 */
	private String season;
	/**
	 * 季节名称
	 */
	private String seasonName;
	/**
	 * 性别编码
	 */
	private String gender;
	/**
	 * 性别名称
	 */
	private String genderName;
	private String sizeKind;
	private String sizeNo;
	/**
	 * 可用库存,因尺码横排故该值无效,见(v1~v20)
	 */
	private BigDecimal qty;
	/**
	 * 该商品的总可用库存
	 */
	private BigDecimal totalQty;
	
	private String v1;
	private String v2;
	private String v3;
	private String v4;
	private String v5;
	private String v6;
	private String v7;
	private String v8;
	private String v9;
	private String v10;
	private String v11;
	private String v12;
	private String v13;
	private String v14;
	private String v15;
	private String v16;
	private String v17;
	private String v18;
	private String v19;
	private String v20;
	public String getLocno() {
		return locno;
	}
	public void setLocno(String locno) {
		this.locno = locno;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
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
	
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getQualityName() {
		return qualityName;
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
	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
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
	public String getSizeKind() {
		return sizeKind;
	}
	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
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
	public BigDecimal getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	public String getV1() {
		return v1;
	}
	public void setV1(String v1) {
		this.v1 = v1;
	}
	public String getV2() {
		return v2;
	}
	public void setV2(String v2) {
		this.v2 = v2;
	}
	public String getV3() {
		return v3;
	}
	public void setV3(String v3) {
		this.v3 = v3;
	}
	public String getV4() {
		return v4;
	}
	public void setV4(String v4) {
		this.v4 = v4;
	}
	public String getV5() {
		return v5;
	}
	public void setV5(String v5) {
		this.v5 = v5;
	}
	public String getV6() {
		return v6;
	}
	public void setV6(String v6) {
		this.v6 = v6;
	}
	public String getV7() {
		return v7;
	}
	public void setV7(String v7) {
		this.v7 = v7;
	}
	public String getV8() {
		return v8;
	}
	public void setV8(String v8) {
		this.v8 = v8;
	}
	public String getV9() {
		return v9;
	}
	public void setV9(String v9) {
		this.v9 = v9;
	}
	public String getV10() {
		return v10;
	}
	public void setV10(String v10) {
		this.v10 = v10;
	}
	public String getV11() {
		return v11;
	}
	public void setV11(String v11) {
		this.v11 = v11;
	}
	public String getV12() {
		return v12;
	}
	public void setV12(String v12) {
		this.v12 = v12;
	}
	public String getV13() {
		return v13;
	}
	public void setV13(String v13) {
		this.v13 = v13;
	}
	public String getV14() {
		return v14;
	}
	public void setV14(String v14) {
		this.v14 = v14;
	}
	public String getV15() {
		return v15;
	}
	public void setV15(String v15) {
		this.v15 = v15;
	}
	public String getV16() {
		return v16;
	}
	public void setV16(String v16) {
		this.v16 = v16;
	}
	public String getV17() {
		return v17;
	}
	public void setV17(String v17) {
		this.v17 = v17;
	}
	public String getV18() {
		return v18;
	}
	public void setV18(String v18) {
		this.v18 = v18;
	}
	public String getV19() {
		return v19;
	}
	public void setV19(String v19) {
		this.v19 = v19;
	}
	public String getV20() {
		return v20;
	}
	public void setV20(String v20) {
		this.v20 = v20;
	}
	
	
}
