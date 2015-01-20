package com.yougou.logistics.city.common.vo;

/**
 * TODO: 拣货单明细、移库回单
 * 
 * @author su.yq
 * @date 2013-10-28 下午5:58:54
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillOmOutstockDtlForQuery {

	private String outstockNo;

	private String locno;

	private String ownerNo;

	private Integer divideId;

	private String creator;

	private Integer realQty;

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

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

	public Integer getDivideId() {
		return divideId;
	}

	public void setDivideId(Integer divideId) {
		this.divideId = divideId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getRealQty() {
		return realQty;
	}

	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}

}
