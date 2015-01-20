package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 
 * 委托业主manager
 * 
 * @author qin.dy
 * @date 2013-9-22 下午1:59:52
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BmDefownerManager extends BaseCrudManager {
	public int deleteFefloc(String locnoStrs) throws ManagerException;
}