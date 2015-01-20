package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-9-30 下午3:42:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillImInstockDtlDto2 extends BaseSizeType {
	//商品编码
	private String itemNo;
	//商品颜色
	private String colorName;
	//商品名称
	private String itemName;
	//款号
	private String styleNo;
	//预上储位
	private String destCellNo;

	private String sizeKind;

	private String sysNo;

	private String sizeNo;

	private BigDecimal itemQty;

	private BigDecimal allCount;

	private BigDecimal allCost;

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

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

	public String getDestCellNo() {
		return destCellNo;
	}

	public void setDestCellNo(String destCellNo) {
		this.destCellNo = destCellNo;
	}

}
