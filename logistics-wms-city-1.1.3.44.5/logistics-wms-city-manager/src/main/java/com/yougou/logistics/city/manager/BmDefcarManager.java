package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 
 * 车辆管理manager
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:06:23
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BmDefcarManager extends BaseCrudManager {
	
	public int deleteFefloc(String locnoStrs) throws ManagerException ;
}