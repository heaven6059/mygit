package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmUntreadMmDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
@Service("billUmUntreadMmDtlManager")
class BillUmUntreadMmDtlManagerImpl extends BaseCrudManagerImpl implements BillUmUntreadMmDtlManager {
	@Resource
	private BillUmUntreadMmDtlService billUmUntreadMmDtlService;

	@Override
	public BaseCrudService init() {
		return billUmUntreadMmDtlService;
	}

	@Override
	public List<BillUmUntreadMmDtl> selectItem(SimplePage page, BillUmUntreadMmDtl dtl) throws ManagerException {
		try {
			return billUmUntreadMmDtlService.selectItem(page, dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItemCount(BillUmUntreadMmDtl dtl) throws ManagerException {
		try {
			return billUmUntreadMmDtlService.selectItemCount(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public void saveUntreadMmDtl(List<BillUmUntreadMmDtl> insertList, List<BillUmUntreadMmDtl> updateList,
			List<BillUmUntreadMmDtl> deleteList, BillUmUntreadMm untread) throws ManagerException {
		try {
			billUmUntreadMmDtlService.saveUntreadMmDtl(insertList, updateList, deleteList, untread);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	public List<BillUmUntreadMmDtl> selectStoreNo(BillUmUntreadMm mm) throws ManagerException {
		try {
			return billUmUntreadMmDtlService.selectStoreNo(mm);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params) throws ManagerException {
		try {
			return billUmUntreadMmDtlService.selectSumQty(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}