package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销到物流 数据传递DTO
 * @author jiang.ys
 *
 */
public class BillLmp2WmsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 单据类型名称
	 */
	private String billName;
	/**
	 * 单据类型
	 */
	private String billType;
	/**
	 * LMP单号
	 */
	private String lmpBillNo;
	/**
	 * WMS仓库名称
	 */
	private String locname;
	/**
	 * WMS仓库
	 */
	private String locno;
	/**
	 * 操作时间
	 */
	private Date operdate;
	/**
	 * WMS数量
	 */
	private BigDecimal qty;
	/**
	 * 品牌库编码
	 */
	private String sysNo;
	/**
	 * WMS单号
	 */
	private String wmsBillNo;
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getLmpBillNo() {
		return lmpBillNo;
	}
	public void setLmpBillNo(String lmpBillNo) {
		this.lmpBillNo = lmpBillNo;
	}
	public String getLocname() {
		return locname;
	}
	public void setLocname(String locname) {
		this.locname = locname;
	}
	public String getLocno() {
		return locno;
	}
	public void setLocno(String locno) {
		this.locno = locno;
	}
	public Date getOperdate() {
		return operdate;
	}
	public void setOperdate(Date operdate) {
		this.operdate = operdate;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public String getWmsBillNo() {
		return wmsBillNo;
	}
	public void setWmsBillNo(String wmsBillNo) {
		this.wmsBillNo = wmsBillNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
