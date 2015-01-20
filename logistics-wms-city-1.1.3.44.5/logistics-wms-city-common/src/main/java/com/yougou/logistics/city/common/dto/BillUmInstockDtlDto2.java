package com.yougou.logistics.city.common.dto;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-28 下午2:03:14
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmInstockDtlDto2 extends BaseItemStyleInfo {
	//预上储位
	private String destCellNo;
	//实际储位
	private String realCellNo;

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
