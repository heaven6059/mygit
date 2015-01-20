package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillStatusLog;
import com.yougou.logistics.city.service.BillStatusLogService;

@Service("billStatusLogManager")
class BillStatusLogManagerImpl extends BaseCrudManagerImpl implements BillStatusLogManager {

	@Resource
	private BillStatusLogService billStatusLogService;

	@Override
	public BaseCrudService init() {
		return billStatusLogService;
	}

	@Override
	public void procInsertBillStatusLog(String locno, String billNo, String billType, String status,
			String description, String operator) throws ManagerException {
		try {
			billStatusLogService.procInsertBillStatusLog(locno, billNo, billType, status, description, operator);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findCountByIm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findCountByIm(params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillStatusLog> findPageByIm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findPageByIm(page, params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findCountByOm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findCountByOm(params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillStatusLog> findPageByOm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findPageByOm(page, params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findCountByUm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findCountByUm(params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillStatusLog> findPageByUm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ManagerException {
		try {
			return billStatusLogService.findPageByUm(page, params, authorityParams, dataAccessRuleEnum);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}