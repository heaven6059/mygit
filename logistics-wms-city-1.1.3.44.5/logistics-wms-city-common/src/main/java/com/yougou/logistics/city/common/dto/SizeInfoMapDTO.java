package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.SizeInfo;

public class SizeInfoMapDTO {
	
	private int i;//尺码下标
	
	private SizeInfo sizeInfo;//尺寸对象

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public SizeInfo getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(SizeInfo sizeInfo) {
		this.sizeInfo = sizeInfo;
	}
}
