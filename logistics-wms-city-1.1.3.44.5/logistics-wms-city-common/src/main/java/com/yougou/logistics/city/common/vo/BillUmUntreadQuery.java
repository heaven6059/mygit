package com.yougou.logistics.city.common.vo;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-13 下午3:10:40
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmUntreadQuery {

	private String startRequestDateStr;//开始请求日期

	private String endRequestDateStr;//结束请求日期

	private String startCreatetmStr;// 起始创建日期

	private String endCreatetmStr;// 结束创建日期

	public String getStartRequestDateStr() {
		return startRequestDateStr;
	}

	public void setStartRequestDateStr(String startRequestDateStr) {
		this.startRequestDateStr = startRequestDateStr;
	}

	public String getEndRequestDateStr() {
		return endRequestDateStr;
	}

	public void setEndRequestDateStr(String endRequestDateStr) {
		this.endRequestDateStr = endRequestDateStr;
	}

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

}
