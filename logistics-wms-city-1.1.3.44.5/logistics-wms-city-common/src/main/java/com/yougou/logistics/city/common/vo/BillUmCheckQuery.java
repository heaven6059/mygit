package com.yougou.logistics.city.common.vo;

/**
 * TODO: 退仓验收单查询条件
 * 
 * @author su.yq
 * @date 2013-11-12 下午12:03:02
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmCheckQuery {

	private String startCreatetmStr;// 起始创建日期

	private String endCreatetmStr;// 结束创建日期

	private String startAudittmStr;// 起始审核日期

	private String endAudittmStr;// 结束审核日期

	private String startChecktmStr;// 起始验收日期

	private String endChecktmStr;// 结束验收日期

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

	public String getStartAudittmStr() {
		return startAudittmStr;
	}

	public void setStartAudittmStr(String startAudittmStr) {
		this.startAudittmStr = startAudittmStr;
	}

	public String getEndAudittmStr() {
		return endAudittmStr;
	}

	public void setEndAudittmStr(String endAudittmStr) {
		this.endAudittmStr = endAudittmStr;
	}

	public String getStartChecktmStr() {
		return startChecktmStr;
	}

	public void setStartChecktmStr(String startChecktmStr) {
		this.startChecktmStr = startChecktmStr;
	}

	public String getEndChecktmStr() {
		return endChecktmStr;
	}

	public void setEndChecktmStr(String endChecktmStr) {
		this.endChecktmStr = endChecktmStr;
	}

}
