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
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmUntreadDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmUntreadDtlManager")
class BillUmUntreadDtlManagerImpl extends BaseCrudManagerImpl implements BillUmUntreadDtlManager {
	@Resource
	private BillUmUntreadDtlService billUmUntreadDtlService;

	@Override
	public BaseCrudService init() {
		return billUmUntreadDtlService;
	}

	@Override
	public void saveUntreadDtl(List<ConBoxDtl> insertList, List<ConBoxDtl> updateList, List<ConBoxDtl> deleteList,
			BillUmUntread untread) throws ManagerException {
		try {
			billUmUntreadDtlService.saveUntreadDtl(insertList, updateList, deleteList, untread);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	public List<BillUmUntreadDtl> selectAllBox(BillUmUntread untread, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmUntreadDtlService.selectAllBox(untread, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findCountByBox(Map<String, Object> params) throws ManagerException {
		try {
			return this.billUmUntreadDtlService.findCountByBox(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillUmUntreadDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ManagerException {
		try {
			return this.billUmUntreadDtlService.findByPageByBox(page, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	public List<ConBoxDtl> select4Box(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmUntreadDtlService.select4Box(params, page, null);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public int select4BoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmUntreadDtlService.select4BoxCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public List<Map<String, Object>> queryPrints(String locno, String keystr) throws ManagerException {
		try {
			return billUmUntreadDtlService.queryPrints(locno, keystr);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmUntreadDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}