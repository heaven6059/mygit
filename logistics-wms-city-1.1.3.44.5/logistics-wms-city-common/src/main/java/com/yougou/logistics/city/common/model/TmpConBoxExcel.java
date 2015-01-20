package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.List;


/**
 * 导入箱明细初始化
 * 
 * @author wanghb
 * @date 2014-10-11 上午11:22:58
 * @version 1.1.3.41
 * @copyright yougou.com
 */
public class TmpConBoxExcel {

	private String ownerNo;
	private String locNo;
	private String conNo;
	private String cellNo;
	private String itemNo;
	private String barcode;
	private String sizeNo;
	private BigDecimal qty;
	private String uuId;
	private List<String> conNoList;
	private String boxType;
	private String lineNo;
	
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}
	public String getLocNo() {
		return locNo;
	}
	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}
	public String getCellNo() {
		return cellNo;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public String getUuId() {
		return uuId;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
	public List<String> getConNoList() {
		return conNoList;
	}
	public void setConNoList(List<String> conNoList) {
		this.conNoList = conNoList;
	}
	public String getBoxType() {
		return boxType;
	}
	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
}