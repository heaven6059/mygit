package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.CsInstockSetting;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-8 下午12:28:36
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class CsInstockSettingDto extends CsInstockSetting {
	private String cellNo;

	private String selectValue;

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}

}
