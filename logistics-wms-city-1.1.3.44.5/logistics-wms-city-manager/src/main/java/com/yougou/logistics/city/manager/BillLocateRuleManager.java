package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillLocateRule;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.common.vo.BillLocateRuleQuery;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillLocateRuleManager extends BaseCrudManager {
	
	/**
	 * 保存储位策略表
	 * @param billLocateRule
	 * @param listCategorys
	 */
	public int saveBillLocateRule(BillLocateRuleQuery query) throws ManagerException;
	
	/**
	 * 批量删除储位策略信息
	 * @param keyStr
	 * @return
	 */
	public int deleteBillLocateRule(List<BillLocateRule> listBillLocateRules) throws ManagerException;
	
}