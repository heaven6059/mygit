package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtlDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.service.BillChRecheckDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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
@Service("billChRecheckDtlManager")
class BillChRecheckDtlManagerImpl extends BaseCrudManagerImpl implements BillChRecheckDtlManager {
	@Resource
	private BillChRecheckDtlService billChRecheckDtlService;

	@Override
	public BaseCrudService init() {
		return billChRecheckDtlService;
	}

	@Override
	public void createChReCheckDtl(List<BillChCheckDtl> dtllist, String recheckWorker,int systemId, int areaSystemId) throws ManagerException {
		try {
			billChRecheckDtlService.createChReCheckDtl(dtllist, recheckWorker,systemId,areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int selectReCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChRecheckDtlService.selectReCheckCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException();
		}
	}

	@Override
	public List<BillChRecheckDtlDto> selectReCheck(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billChRecheckDtlService.selectReCheck(map, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException();
		}
	}

	@Override
	public List<BillChRecheckDtl> selectCellNo(BillChRecheckDtl check) throws ManagerException {
		try {
			return billChRecheckDtlService.selectCellNo(check);
		} catch (ServiceException e) {
			throw new ManagerException();
		}
	}

	public ResultVo updateReCheckDtl(List<BillChRecheckDtl> insertList, List<BillChRecheckDtl> updateList,
			List<BillChRecheckDtl> deleteList, String recheckNo, String checkNo, String locno) throws ManagerException {
		try {
			return billChRecheckDtlService.updateReCheckDtl(insertList, updateList, deleteList, recheckNo, checkNo,
					locno);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void chReCheckAudit(String dtls, String locno) throws ManagerException {
		try {
			billChRecheckDtlService.chReCheckAudit(dtls, locno);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void saveByPlan(BillChRecheckDtl check) throws ManagerException {
		try {
			billChRecheckDtlService.saveByPlan(check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void resetPlan(BillChRecheckDtl check) throws ManagerException {
		try {
			billChRecheckDtlService.resetPlan(check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billChRecheckDtlService.selectSumQty(map, authorityParams);
	}

	@Override
	public SumUtilMap<String, Object> selectChReCheckSumQty(
			Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billChRecheckDtlService.selectChReCheckSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}