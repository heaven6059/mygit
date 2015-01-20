package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 容器资料manager
 * 
 * @author qin.dy
 * @date 2013-9-22 下午3:02:47
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface SysDefcontainerManager extends BaseCrudManager {
	public int deleteFefloc(String locnoStrs) throws ManagerException ;
}