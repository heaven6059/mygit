package com.yougou.logistics.city.common.enums.ordermonitor;

import java.math.BigDecimal;

/**
 * 订单监控包含的机构类型
 * 
 * @author xian.yq
 * @date 2013-9-2 上午10:47:39
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum MonitorStoreType {
	
	STORETYPE0(new BigDecimal(0),"工厂仓"),
	STORETYPE1(new BigDecimal(1),"中转仓"),
	STORETYPE2(new BigDecimal(2),"城市仓"),
	STORETYPE3(new BigDecimal(3),"门店");
	
	private BigDecimal type;
	private String name;
	
	private MonitorStoreType(BigDecimal type, String name) {
		this.type = type;
		this.name = name;
	}

	public BigDecimal getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
