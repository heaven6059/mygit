package com.yougou.logistics.city.common.vo;

import java.util.Date;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-13 下午4:34:49
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmReceiptQuery {

	private String startCreatetmStr;// 起始创建日期

	private String endCreatetmStr;// 结束创建日期

	private String startReciveDateStr;// 起始收货日期

	private String endReciveDateStr;// 结束收货日期

	public String getStartCreatetmStr() {
		return startCreatetmStr;
	}

	public void setStartCreatetmStr(String startCreatetmStr) {
		this.startCreatetmStr = startCreatetmStr;
	}

	public String getEndCreatetmStr() {
		return endCreatetmStr;
	}

	public void setEndCreatetmStr(String endCreatetmStr) {
		this.endCreatetmStr = endCreatetmStr;
	}

	public String getStartReciveDateStr() {
		return startReciveDateStr;
	}

	public void setStartReciveDateStr(String startReciveDateStr) {
		this.startReciveDateStr = startReciveDateStr;
	}

	public String getEndReciveDateStr() {
		return endReciveDateStr;
	}

	public void setEndReciveDateStr(String endReciveDateStr) {
		this.endReciveDateStr = endReciveDateStr;
	}

}
