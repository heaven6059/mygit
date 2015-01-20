package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class SizeComposeDTO extends BaseSizeType {

	private String checkNo;

	private String untreadNo;

	public String getUntreadNo() {
		return untreadNo;
	}

	public void setUntreadNo(String untreadNo) {
		this.untreadNo = untreadNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	private String itemNo;

	private String itemName;

	private String sizeNo;

	private String sysNo;

	private String sizeKind;

	private BigDecimal checkQty;
	private BigDecimal untreadQty;

	public BigDecimal getUntreadQty() {
		return untreadQty;
	}

	public void setUntreadQty(BigDecimal untreadQty) {
		this.untreadQty = untreadQty;
	}

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	private String styleNo;

	/**
	 * 颜色
	 */
	private String colorNoStr;

	/**
	 * 品牌
	 */
	private String brandNoStr;

	private BigDecimal allCount;

	private BigDecimal allCost;

	public BigDecimal getAllCount() {
		return allCount;
	}

	public void setAllCount(BigDecimal allCount) {
		this.allCount = allCount;
	}

	public BigDecimal getAllCost() {
		return allCost;
	}

	public void setAllCost(BigDecimal allCost) {
		this.allCost = allCost;
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

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorNoStr() {
		return colorNoStr;
	}

	public void setColorNoStr(String colorNoStr) {
		this.colorNoStr = colorNoStr;
	}

	public String getBrandNoStr() {
		return brandNoStr;
	}

	public void setBrandNoStr(String brandNoStr) {
		this.brandNoStr = brandNoStr;
	}

}