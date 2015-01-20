package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface BillStoreRuleManager extends BaseCrudManager {
	
	/**
	 * 批量删除按箱规则信息
	 * @param keyStr
	 * @return
	 */
	public int deleteStoreRuleBatch(String locno, String keyStr, String tempStr) throws ManagerException;
}