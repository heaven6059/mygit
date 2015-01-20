package com.yougou.logistics.city.common.enums;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-10-21 下午12:15:51
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public enum ContainerTypeEnums {

	//作业类型  A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板;HM-移库;CH-盘点;CA-库存调整;OD-装车出库;
	A("A"), B("B"), C("C"), D("D"), E("E"), F("F"), P("P"),HM("HM"),CH("CH"),CA("CA"),OD("OD");

	private String optBillType;

	private ContainerTypeEnums(String optBillType) {
		this.optBillType = optBillType;
	}

	public String getOptBillType() {
		return this.optBillType;
	}
}
