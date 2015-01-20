package com.yougou.logistics.city.common.dto;


import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * 用于查询箱号的明细信息
 * @author zuo.sw
 *
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class CnLabelQueryDto {
	
	private String recedeNo;
	
	private String labelNo;
	
	private String recheckNo;

	public String getRecedeNo() {
		return recedeNo;
	}

	public void setRecedeNo(String recedeNo) {
		this.recedeNo = recedeNo;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getRecheckNo() {
		return recheckNo;
	}

	public void setRecheckNo(String recheckNo) {
		this.recheckNo = recheckNo;
	}
	
}
