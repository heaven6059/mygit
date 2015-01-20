package com.yougou.logistics.city.common.enums.ordermonitor;

import java.math.BigDecimal;

/**
 * 订单监控包含的单据类型
 * 
 * @author xian.yq
 * @date 2013-9-2 上午10:46:10
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum MonitorBillType {
	
	BILLTYPE1302(new BigDecimal(1302),"发货单"),
	BILLTYPE1304(new BigDecimal(1304),"验收单"),
	BILLTYPE1307(new BigDecimal(1307),"配送出库单"),
	BILLTYPE1303(new BigDecimal(1307),"配送入库单");
	
	private BigDecimal type;
	private String name;
	
	private MonitorBillType(BigDecimal type, String name) {
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
