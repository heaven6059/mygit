package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.model.BillUmInstockDirect;

/**
 * TODO: 定位信息dto
 * 
 * @author luo.hl
 * @date 2013-11-18 下午1:57:24
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmInstockDirectDto extends BillUmInstockDirect {
	private Long rowIds;
	private String itemName;
	private String styleNo;
	private String colorName;
	private String unitName;
	private BigDecimal sumQty;

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

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getRowIds() {
		return rowIds;
	}

	public void setRowIds(Long rowIds) {
		this.rowIds = rowIds;
	}

	public BigDecimal getSumQty() {
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}

}
