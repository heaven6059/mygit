package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillChDifferent;
import com.yougou.logistics.city.common.model.SystemUser;

public interface BillChDifferentManager extends BaseCrudManager {
	/**
	 * 生成差异单
	 * @return
	 * @throws ManagerException
	 */
	public String addDiff(BillChDifferent billChDifferent,SystemUser user) throws ManagerException;
}