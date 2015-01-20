package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-8 上午11:27:04
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillWmRecedeDispatchDtlDTO {

	private String locno;//仓别

	private String recedeNo;//出库单号

	private String itemNo;//商品编码

	private String itemName;//商品名称

	private String styleNo;//款号

	private String colorDesc;//颜色

	private String sizeDesc;//尺码

	private int recedeQty;//计划退厂数量

	private int availableRecedeQty;//可退厂数量

	private int noenoughQty;//缺量

	private int differenceQty;//可用数量 

	private BigDecimal volume;//体积

	private BigDecimal weight;//重量 
	
	private String sizeNo;//尺码
	
	private String colorName;//颜色
	
	private int usableQty;//可用数量

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
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

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorDesc() {
		return colorDesc;
	}

	public void setColorDesc(String colorDesc) {
		this.colorDesc = colorDesc;
	}

	public String getSizeDesc() {
		return sizeDesc;
	}

	public void setSizeDesc(String sizeDesc) {
		this.sizeDesc = sizeDesc;
	}

	public int getRecedeQty() {
		return recedeQty;
	}

	public void setRecedeQty(int recedeQty) {
		this.recedeQty = recedeQty;
	}

	public int getAvailableRecedeQty() {
		return availableRecedeQty;
	}

	public void setAvailableRecedeQty(int availableRecedeQty) {
		this.availableRecedeQty = availableRecedeQty;
	}

	public int getNoenoughQty() {
		return noenoughQty;
	}

	public void setNoenoughQty(int noenoughQty) {
		this.noenoughQty = noenoughQty;
	}

	public int getDifferenceQty() {
		return differenceQty;
	}

	public void setDifferenceQty(int differenceQty) {
		this.differenceQty = differenceQty;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public int getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(int usableQty) {
		this.usableQty = usableQty;
	}
}
