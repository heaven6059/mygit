package com.yougou.logistics.city.common.vo;

import java.util.List;
import com.yougou.logistics.city.common.model.BillLocateRule;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-6 下午8:02:26
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillLocateRuleQuery {

	private int operStatus;
	private BillLocateRule billLocateRule;
	private List<BillLocateRuleDtl> listInserteds;
	private List<BillLocateRuleDtl> listdeleteds;

	public int getOperStatus() {
		return operStatus;
	}

	public void setOperStatus(int operStatus) {
		this.operStatus = operStatus;
	}

	public BillLocateRule getBillLocateRule() {
		return billLocateRule;
	}

	public void setBillLocateRule(BillLocateRule billLocateRule) {
		this.billLocateRule = billLocateRule;
	}

	public List<BillLocateRuleDtl> getListInserteds() {
		return listInserteds;
	}

	public void setListInserteds(List<BillLocateRuleDtl> listInserteds) {
		this.listInserteds = listInserteds;
	}

	public List<BillLocateRuleDtl> getListdeleteds() {
		return listdeleteds;
	}

	public void setListdeleteds(List<BillLocateRuleDtl> listdeleteds) {
		this.listdeleteds = listdeleteds;
	}
}
