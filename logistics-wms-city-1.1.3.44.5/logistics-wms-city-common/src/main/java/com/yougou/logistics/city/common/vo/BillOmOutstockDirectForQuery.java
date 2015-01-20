package com.yougou.logistics.city.common.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yougou.logistics.city.common.model.BillOmOutstockDirect;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
public class BillOmOutstockDirectForQuery {

	private String ownerNo;

	private String outstockType;

	private String operateType;

	private String pickType;

	private String batchNo;

	private String locateNo;

	private String expType;

	private String lookupCode;

	private String storeNo;

	private String areaNo;

	private String locno;

	private String creator;

	private String pickingPeople;

	private String wareNo;

	private String cellType;

	private String outstockPeople;
	
	private BigDecimal workQty;
	
	private String sortType;
	
	private List<BillOmOutstockDirect> dlist;
	
	private int isFloor;

	public String getPickingPeople() {
		return pickingPeople;
	}

	public void setPickingPeople(String pickingPeople) {
		this.pickingPeople = pickingPeople;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getOutstockType() {
		return outstockType;
	}

	public void setOutstockType(String outstockType) {
		this.outstockType = outstockType;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getPickType() {
		return pickType;
	}

	public void setPickType(String pickType) {
		this.pickType = pickType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLocateNo() {
		return locateNo;
	}

	public void setLocateNo(String locateNo) {
		this.locateNo = locateNo;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getWareNo() {
		return wareNo;
	}

	public void setWareNo(String wareNo) {
		this.wareNo = wareNo;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public String getOutstockPeople() {
		return outstockPeople;
	}

	public void setOutstockPeople(String outstockPeople) {
		this.outstockPeople = outstockPeople;
	}

	public List<BillOmOutstockDirect> getDlist() {
		return dlist;
	}

	public void setDlist(List<BillOmOutstockDirect> dlist) {
		this.dlist = dlist;
	}

	public BigDecimal getWorkQty() {
		return workQty;
	}

	public void setWorkQty(BigDecimal workQty) {
		this.workQty = workQty;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getIsFloor() {
		return isFloor;
	}

	public void setIsFloor(int isFloor) {
		this.isFloor = isFloor;
	}
}