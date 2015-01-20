package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface BillChReportManager extends BaseCrudManager {
	
	/**
	 * 总计
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Integer> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams)throws ManagerException;
}