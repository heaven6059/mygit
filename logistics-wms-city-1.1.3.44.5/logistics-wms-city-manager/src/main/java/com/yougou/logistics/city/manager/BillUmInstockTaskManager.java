package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * TODO: 退仓上架任务
 * 
 * @author luo.hl
 * @date 2013-11-13 上午11:51:49
 * @version 0.1.0 
 * @copyright yougou.com 
 */

public interface BillUmInstockTaskManager extends BaseCrudManager {

	/**
	 * 取消定位
	 * @param rowStr
	 * @throws ManagerException
	 */
	public void cancelDirect(String rowStr) throws ManagerException;

	/**
	 * 发单
	 * @param curUser
	 * @param locNo
	 * @param rowStrs
	 * @param instockWorker
	 * @throws ManagerException
	 */
	public void createInstock(String curUser, String locNo, String rowStrs, String instockWorker)
			throws ManagerException;
}
