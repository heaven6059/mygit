package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.service.BillUmCheckTaskService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmCheckTaskManager")
class BillUmCheckTaskManagerImpl extends BaseCrudManagerImpl implements BillUmCheckTaskManager {
	
	@Resource
	private BillUmCheckTaskService billUmCheckTaskService;
	
	@Resource
	private ProcCommonService  procCommonService;

	@Override
	public BaseCrudService init() {
		return billUmCheckTaskService;
	}

	@Override
	public void saveUmCheckTask(BillUmCheckTask umCheckTask, List<BillUmUntread> untreadList) throws ManagerException {
		try {
			billUmCheckTaskService.saveUmCheckTask(umCheckTask, untreadList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteUmCheckTask(List<BillUmCheckTask> taskList) throws ManagerException {
		try {
			billUmCheckTaskService.deleteUmCheckTask(taskList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void auditUmCheckTask(String keyStr, String loginName,
			String userName, String locno) throws ManagerException {
		try {
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String checkTaskNo : keys) {
					 procCommonService.auditUmCheckTask(locno, checkTaskNo, loginName, userName);
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

    @Override
    public Map<String, Object> selectSumQty(Map<String, Object> params,
            AuthorityParams authorityParams) throws ManagerException {
        try {
            return this.billUmCheckTaskService.selectSumQty(params,authorityParams);
        } catch (ServiceException e) {
            throw new ManagerException(e.getMessage(), e);
        }
    }

}