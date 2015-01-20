package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface BmDeflocManager extends BaseCrudManager {
	
	/**
	 * 删除仓别
	 * @param locnoStrs
	 * @return
	 * @throws ManagerException
	 */
	public int deleteFefloc(String locnoStrs) throws ManagerException;
	
	
	/**
	 * 校验仓别下是否有用户
	 * @param locnoStrs
	 * @return
	 * @throws ManagerException
	 */
	public boolean findIsLocUser(String locnoStrs) throws ManagerException;
	
	
}