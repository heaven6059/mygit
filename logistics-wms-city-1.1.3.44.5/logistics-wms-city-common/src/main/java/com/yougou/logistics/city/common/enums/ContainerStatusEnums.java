package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-10-21 下午12:18:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum ContainerStatusEnums {

	//作业状态  0-未使用;1-已使用;
	STATUS0("0"), STATUS1("1");

	private String containerStatus;

	private ContainerStatusEnums(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	public String getContainerStatus() {
		return this.containerStatus;
	}
}
