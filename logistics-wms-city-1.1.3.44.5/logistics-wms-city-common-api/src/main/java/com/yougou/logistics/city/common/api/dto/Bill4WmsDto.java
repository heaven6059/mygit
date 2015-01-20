package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-5-14 下午2:28:25
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class Bill4WmsDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billType;
	private String locno;
	private String locName;
	private String billName;
	private String wmsBillNo;
	private int qty;
	private Date operDate;
	private String sysNo;

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getWmsBillNo() {
		return wmsBillNo;
	}

	public void setWmsBillNo(String wmsBillNo) {
		this.wmsBillNo = wmsBillNo;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Date getOperDate() {
		return operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

}
