package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 
 * 调用统一存储过程manager
 * 
 * @author qin.dy
 * @date 2013-9-26 下午4:02:04
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("procCommonManager")
class ProcCommonManagerImpl extends BaseCrudManagerImpl implements ProcCommonManager {

	@Resource
	private ProcCommonService procCommonService;

	@Override
	public BaseCrudService init() {
		return procCommonService;
	}

	public String procGetSheetNo(String locNo, String strpapertype) throws ManagerException {

		try {
			return procCommonService.procGetSheetNo(locNo, strpapertype);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public Map<String, String> procGetContainerNoBase(String locno, String strpapertype, String userId, String getType,
			String getNum, String useType, String containerMetrial) throws ManagerException {
		try {
			return procCommonService.procGetContainerNoBase(locno, strpapertype, userId, getType, getNum, useType,
					containerMetrial);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

//	public boolean procImInstockDirectByReceipt(String strLocno, String strReceiptNo, String strWorkerNo)
//			throws ManagerException {
//		try {
//			return procCommonService.procImInstockDirectByReceipt(strLocno, strReceiptNo, strWorkerNo);
//		} catch (ServiceException e) {
//			throw new ManagerException(e);
//		}
//	}
}