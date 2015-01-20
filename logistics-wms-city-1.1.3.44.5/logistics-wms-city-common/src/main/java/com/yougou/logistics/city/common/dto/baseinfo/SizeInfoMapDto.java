package com.yougou.logistics.city.common.dto.baseinfo;

import com.yougou.logistics.city.common.model.SizeInfo;

/**
 * TODO: 尺码DTO
 * 
 * @author su.yq
 * @date 2014-1-24 下午2:18:38
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class SizeInfoMapDto {

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
