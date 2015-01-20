package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface BmCircleManager extends BaseCrudManager {
	/**
	 * 校验商圈下是否有客户
	 * @param locnoStrs
	 * @return
	 * @throws ManagerException
	 */
	public boolean findIsStore(String circleNoStrs) throws ManagerException;
	/**
	 * 删除商圈
	 * @param circleNoStrs
	 * @return
	 * @throws ManagerException
	 */
	public int deleteCircle(String circleNoStrs) throws ManagerException;
}