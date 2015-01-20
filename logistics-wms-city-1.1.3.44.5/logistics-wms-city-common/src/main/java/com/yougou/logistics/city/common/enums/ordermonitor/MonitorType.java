package com.yougou.logistics.city.common.enums.ordermonitor;

import java.math.BigDecimal;



/**
 * 订单监控类型
 * 
 * @author xian.yq
 * @date 2013-9-2 下午12:29:55
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum MonitorType {
	
	//TYPE0(MonitorStoreType.STORETYPE0,MonitorBillType.BILLTYPE1302,"发货单 {0} 品项数: {1} 发货量: {2} 计划到货日期: {3} 累计到货量: {4}"),
	TYPE1(MonitorStoreType.STORETYPE1,MonitorBillType.BILLTYPE1304,"验收单 {0} 品项数: {1} 验收量: {2} 验收日期: {3} 累计验收量: {4}"),
	TYPE2(MonitorStoreType.STORETYPE1,MonitorBillType.BILLTYPE1307,"配送出库单 {0} 品项数: {1} 配出量: {2} 出库日期: {3} 累计出库量: {4}"),
	TYPE3(MonitorStoreType.STORETYPE2,MonitorBillType.BILLTYPE1304,"验收单 {0} 品项数: {1} 验收量: {2} 验收日期: {3} 累计验收量: {4}"),
	TYPE4(MonitorStoreType.STORETYPE2,MonitorBillType.BILLTYPE1307,"配送出库单 {0} 品项数: {1} 配出量: {2} 出库日期: {3} 累计出库量: {4}"),
	TYPE5(MonitorStoreType.STORETYPE3,MonitorBillType.BILLTYPE1303,"配送入库单 {0} 品项数: {1} 入库量: {2} 入库日期: {3} 累计入库量: {4}");
	
	private MonitorStoreType storeType;
	private MonitorBillType billType;
	private String text;
	
	private MonitorType(MonitorStoreType storeType, MonitorBillType billType, String text) {
		this.storeType = storeType;
		this.billType = billType;
		this.text = text;
	}

	public MonitorStoreType getStoreType() {
		return storeType;
	}

	public MonitorBillType getBillType() {
		return billType;
	}
	
	public String getText() {
		return text;
	}

	public static MonitorType getTypeOfVal(BigDecimal storeType,BigDecimal billType) {
		if (storeType==null || billType==null) {
			return null;
		}
		MonitorType[] typeArr = MonitorType.values();
		for (MonitorType monitorType : typeArr) {
			BigDecimal tempStoreType = monitorType.getStoreType().getType();
			BigDecimal tempBillType = monitorType.getBillType().getType();
			if (tempStoreType.equals(storeType) && tempBillType.equals(billType)) {
				return monitorType;
			}
		}
		return null;
	}
}
