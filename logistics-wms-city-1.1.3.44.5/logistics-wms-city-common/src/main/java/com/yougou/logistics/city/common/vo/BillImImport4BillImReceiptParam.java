package com.yougou.logistics.city.common.vo;

/**
 * 请写出类的用途 
 * @author luohaili
 * @date  2013-09-25 10:24:56
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public class BillImImport4BillImReceiptParam {
	private int rows;
	private int page;
	private String locno;

	private String ownerNo;

	private String brandNo;

	private String transNo;

	private String orderDateStart;

	private String orderDateEnd;

	private String requestDateStart;

	private String requestDateEnd;

	private String importNo;

	private long startRowNum;

	private long endRowNum;

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public long getStartRowNum() {
		return startRowNum;
	}

	public void setStartRowNum(long startRowNum) {
		this.startRowNum = startRowNum;
	}

	public long getEndRowNum() {
		return endRowNum;
	}

	public void setEndRowNum(long endRowNum) {
		this.endRowNum = endRowNum;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getOrderDateStart() {
		return orderDateStart;
	}

	public void setOrderDateStart(String orderDateStart) {
		this.orderDateStart = orderDateStart;
	}

	public String getOrderDateEnd() {
		return orderDateEnd;
	}

	public void setOrderDateEnd(String orderDateEnd) {
		this.orderDateEnd = orderDateEnd;
	}

	public String getRequestDateStart() {
		return requestDateStart;
	}

	public void setRequestDateStart(String requestDateStart) {
		this.requestDateStart = requestDateStart;
	}

	public String getRequestDateEnd() {
		return requestDateEnd;
	}

	public void setRequestDateEnd(String requestDateEnd) {
		this.requestDateEnd = requestDateEnd;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}