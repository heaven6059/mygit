package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;

import com.yougou.logistics.city.common.model.BillOmOutstockDtl;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-14 下午5:14:17
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillOmOutstockDtlDto extends BillOmOutstockDtl {

	private String itemName;
	private String styleNo;
	private String colorName;
	private String ownerName;
	private BigDecimal diffQty;
	private int totalItemQty;//总计划数量
	private int totalRealQty;//总拣货数量
	private BigDecimal packageNum;//装箱数量
	
	private String sysNo;
	private String sizeKind;
	private String barcode;
	private String creator;
	private String creatorname;
	private BigDecimal goodContentQty;
	private BigDecimal instockQty;
	private BigDecimal packageNoRecheckQty; 
	private String panNo;
	
	
	
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

	public BigDecimal getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
	}

	public int getTotalItemQty() {
		return totalItemQty;
	}

	public void setTotalItemQty(int totalItemQty) {
		this.totalItemQty = totalItemQty;
	}

	public int getTotalRealQty() {
		return totalRealQty;
	}

	public void setTotalRealQty(int totalRealQty) {
		this.totalRealQty = totalRealQty;
	}

	public BigDecimal getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(BigDecimal packageNum) {
		this.packageNum = packageNum;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getGoodContentQty() {
		return goodContentQty;
	}

	public void setGoodContentQty(BigDecimal goodContentQty) {
		this.goodContentQty = goodContentQty;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public BigDecimal getInstockQty() {
		return instockQty;
	}

	public void setInstockQty(BigDecimal instockQty) {
		this.instockQty = instockQty;
	}

	public BigDecimal getPackageNoRecheckQty() {
		return packageNoRecheckQty;
	}

	public void setPackageNoRecheckQty(BigDecimal packageNoRecheckQty) {
		this.packageNoRecheckQty = packageNoRecheckQty;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}
	
}
