package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

/**
 * TODO: 出库调度明细信息
 * 
 * @author su.yq
 * @date 2013-11-5 下午2:09:53
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillOmExpDispatchDtlDTO {
	
	private String locno;//仓别
	
	private String expNo;//出库单号
	
	private String poNo;//合同号
	
	private String storeNo;//客户编码
	
	private String storeName;//客户名称
	
	private String itemNo;//商品编码
	
	private String itemName;//商品名称
	
	private String styleNo;//款号
	
	private String colorName;//颜色
	
	private String sizeNo;//尺码
	
	private int expQty;//数量
	
	private int itemQty;//计划数量
	
	private int noenoughQty;//缺量
	
	private int differenceQty;//可用数量 
	
	private BigDecimal volume;//体积

	private BigDecimal weight;//重量 
	
	private int usableQty;//可用数量
	
	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
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

	public int getExpQty() {
		return expQty;
	}

	public void setExpQty(int expQty) {
		this.expQty = expQty;
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

	public int getItemQty() {
		return itemQty;
	}

	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(int usableQty) {
		this.usableQty = usableQty;
	}
}
