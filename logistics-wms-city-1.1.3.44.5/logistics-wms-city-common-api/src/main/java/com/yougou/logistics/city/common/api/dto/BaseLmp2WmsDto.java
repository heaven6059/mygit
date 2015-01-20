package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午2:58:34
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BaseLmp2WmsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String billType;//接口编码

	private String billName;//接口名称

	private int qty;//记录数

	private String sysNo;//品牌库编码

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
}
