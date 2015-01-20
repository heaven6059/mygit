package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TODO: 业务单据异常数量DTO
 * 
 * @author ye.kl
 * @date 2014-1-23 上午11:51:54
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUnusualDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 区域编码
	 */
	private String zoneNo;
	/**
	 * 仓别编码
	 */
	private String locNo;
	/**
	 * 已入库未执行数量
	 */
	private BigDecimal rQty;
	/**
	 * 已下分拣任务未执行数量
	 */
	private BigDecimal oQty;
	/**
	 * 已下上架任务未上架数量
	 */
	private BigDecimal iQty;
	/**
	 * 已封箱完成未出库数量
	 */
	private BigDecimal reQty;
	/**
	 * 已退仓未整理上架数量
	 */
	private BigDecimal cQty;

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public String getLocNo() {
		return locNo;
	}

	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}

	public BigDecimal getrQty() {
		return rQty;
	}

	public void setrQty(BigDecimal rQty) {
		this.rQty = rQty;
	}

	public BigDecimal getoQty() {
		return oQty;
	}

	public void setoQty(BigDecimal oQty) {
		this.oQty = oQty;
	}

	public BigDecimal getiQty() {
		return iQty;
	}

	public void setiQty(BigDecimal iQty) {
		this.iQty = iQty;
	}

	public BigDecimal getReQty() {
		return reQty;
	}

	public void setReQty(BigDecimal reQty) {
		this.reQty = reQty;
	}

	public BigDecimal getcQty() {
		return cQty;
	}

	public void setcQty(BigDecimal cQty) {
		this.cQty = cQty;
	}

}
