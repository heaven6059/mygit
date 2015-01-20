package com.yougou.logistics.city.common.model;

import com.yougou.logistics.city.common.enums.BaseInfoDisableAndEnabledEnums;

public class BillStoreRule {

	private String locno;
	
	private String ruleNo;// 规则代号

	private String ruleName;// 规则名称

	private String storeBasic;// 店分组依据 1：按预发货量 2：按门店编码

	private Short storeSort;// 店排序规则 1：大->小 2：小->大

	private String boxBasic;// 箱分组依据 1：按预发货量 2：按门店数量

	private Short boxFlag;// 是否拼箱为一组：0： 否 1 ：是

	private String cargoBasic;// 分货依据：1： 按总订货量 1 ：按门店代号

	private Short cargoSort;// 分货排序：1：大->小 2：小->大

	private Short boxSort;// 箱号排序顺序 1：按箱号大到小 2：按箱号小到大

	private String boxType;// 箱码类型：1： 先整箱后拼箱 2 ：先拼箱后整箱

	private String status;// 启用状态：0：禁用 1：启用
	
	private String tempNo;//模板名称

	private Integer groupA;// A组范围值

	private Integer groupB;// B组范围值

	private Integer groupC;// C组范围值

	private Integer groupD;// D组范围值

	private Integer groupE;// E组范围值

	private Integer groupF;// F组范围值

	private Integer groupG;// G组范围值

	private Integer groupH;// H组范围值

	private Integer groupI;// I组范围值

	private Integer groupJ;// J组范围值

	/** 附加属性 **/
	private String statusStr;// 状态显示

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getStoreBasic() {
		return storeBasic;
	}

	public void setStoreBasic(String storeBasic) {
		this.storeBasic = storeBasic;
	}

	public Short getStoreSort() {
		return storeSort;
	}

	public void setStoreSort(Short storeSort) {
		this.storeSort = storeSort;
	}

	public String getBoxBasic() {
		return boxBasic;
	}

	public void setBoxBasic(String boxBasic) {
		this.boxBasic = boxBasic;
	}

	public Short getBoxFlag() {
		return boxFlag;
	}

	public void setBoxFlag(Short boxFlag) {
		this.boxFlag = boxFlag;
	}

	public String getCargoBasic() {
		return cargoBasic;
	}

	public void setCargoBasic(String cargoBasic) {
		this.cargoBasic = cargoBasic;
	}

	public Short getCargoSort() {
		return cargoSort;
	}

	public void setCargoSort(Short cargoSort) {
		this.cargoSort = cargoSort;
	}

	public Short getBoxSort() {
		return boxSort;
	}

	public void setBoxSort(Short boxSort) {
		this.boxSort = boxSort;
	}

	public String getBoxType() {
		return boxType;
	}

	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusStr = BaseInfoDisableAndEnabledEnums.getTextByStatus(this.status);
	}

	public Integer getGroupA() {
		return groupA;
	}

	public void setGroupA(Integer groupA) {
		this.groupA = groupA;
	}

	public Integer getGroupB() {
		return groupB;
	}

	public void setGroupB(Integer groupB) {
		this.groupB = groupB;
	}

	public Integer getGroupC() {
		return groupC;
	}

	public void setGroupC(Integer groupC) {
		this.groupC = groupC;
	}

	public Integer getGroupD() {
		return groupD;
	}

	public void setGroupD(Integer groupD) {
		this.groupD = groupD;
	}

	public Integer getGroupE() {
		return groupE;
	}

	public void setGroupE(Integer groupE) {
		this.groupE = groupE;
	}

	public Integer getGroupF() {
		return groupF;
	}

	public void setGroupF(Integer groupF) {
		this.groupF = groupF;
	}

	public Integer getGroupG() {
		return groupG;
	}

	public void setGroupG(Integer groupG) {
		this.groupG = groupG;
	}

	public Integer getGroupH() {
		return groupH;
	}

	public void setGroupH(Integer groupH) {
		this.groupH = groupH;
	}

	public Integer getGroupI() {
		return groupI;
	}

	public void setGroupI(Integer groupI) {
		this.groupI = groupI;
	}

	public Integer getGroupJ() {
		return groupJ;
	}

	public void setGroupJ(Integer groupJ) {
		this.groupJ = groupJ;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getTempNo() {
		return tempNo;
	}

	public void setTempNo(String tempNo) {
		this.tempNo = tempNo;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}
	
}