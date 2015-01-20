package com.yougou.logistics.city.common.dto;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-13 上午11:13:46
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmUntreadDtlDTO {

	private String locno;

	private String untreadNo;

	private String itemNo;

	private String sizeNo;

	private int untreadQty;

	private String styleNo;

	private String colorName;

	private String itemName;

	private String packUnit;

	private String sysNo;

	private String sysNoStr;

	private String barcode;

	private String boxNo;

	private int planQty;

	private int checkQty;

	private int packQty;

	public String getUntreadNo() {
		return untreadNo;
	}

	public void setUntreadNo(String untreadNo) {
		this.untreadNo = untreadNo;
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

	public int getUntreadQty() {
		return untreadQty;
	}

	public void setUntreadQty(int untreadQty) {
		this.untreadQty = untreadQty;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}

	public int getPlanQty() {
		return planQty;
	}

	public void setPlanQty(int planQty) {
		this.planQty = planQty;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public int getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(int checkQty) {
		this.checkQty = checkQty;
	}

	public int getPackQty() {
		return packQty;
	}

	public void setPackQty(int packQty) {
		this.packQty = packQty;
	}
}
