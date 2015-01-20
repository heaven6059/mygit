package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 存储过程调用manager
 * 
 * @author qin.dy
 * @date 2013-10-21 上午11:21:37
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface ProcCommonManager extends BaseCrudManager {

	public String procGetSheetNo(String locNo, String strpapertype) throws ManagerException;

	public Map<String, String> procGetContainerNoBase(String locno, String strpapertype, String userId, String getType,
			String getNum, String useType, String containerMetrial) throws ManagerException;

//	public boolean procImInstockDirectByReceipt(String strLocno, String strReceiptNo, String strWorkerNo)
//			throws ManagerException;
}
