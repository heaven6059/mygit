package com.yougou.logistics.city.common.model;

/**
 * 明细权限参数
 * 
 * @author luo.hl
 * @date 2014-4-11 下午12:18:20
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityDtlTableBean {
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 单据字段名称
	 */
	private String billNoClumn;
	/**
	 * 单据编号
	 */
	private String billNo;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBillNoClumn() {
		return billNoClumn;
	}

	public void setBillNoClumn(String billNoClumn) {
		this.billNoClumn = billNoClumn;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

}
