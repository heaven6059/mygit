package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * @author wei.b
 * @email wei.b@yougou.com
 * create time: 2013-6-18
 */
public interface LogisticsBaseManager extends BaseCrudManager {

	public abstract int findCount() throws ManagerException;

}
