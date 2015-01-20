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
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmPrepareDivideDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@Service("billOmPrepareDivideDtlManager")
class BillOmPrepareDivideDtlManagerImpl extends BaseCrudManagerImpl implements BillOmPrepareDivideDtlManager {
	@Resource
	private BillOmPrepareDivideDtlService billOmPrepareDivideDtlService;

	@Override
	public BaseCrudService init() {
		return billOmPrepareDivideDtlService;
	}

	@Override
	public int findPrepareDivideDetailCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideDtlService.findPrepareDivideDetailCount(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findPrepareDivideDetail(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideDtlService.findPrepareDivideDetail(page, map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}


	@Override
	public List<BillImReceiptDtl> selectItemDetail4Prepare(Map<String, Object> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideDtlService.selectItemDetail4Prepare(map, page,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItemDetail4PrepareCount(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideDtlService.selectItemDetail4PrepareCount(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}


	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) {
		return this.billOmPrepareDivideDtlService.selectSumQty(map,authorityParams);
	}

}