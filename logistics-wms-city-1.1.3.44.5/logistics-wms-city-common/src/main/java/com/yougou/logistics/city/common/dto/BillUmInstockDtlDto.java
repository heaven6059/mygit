package com.yougou.logistics.city.common.dto;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-28 下午2:02:27
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmInstockDtlDto {
	private String locno;
	private String ownerNo;
	private String instockNo;
	private String itemNo;
	private String destCellNo;
	private String realCellNo;

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

	public String getInstockNo() {
		return instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
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

	public String getRealCellNo() {
		return realCellNo;
	}

	public void setRealCellNo(String realCellNo) {
		this.realCellNo = realCellNo;
	}

}
