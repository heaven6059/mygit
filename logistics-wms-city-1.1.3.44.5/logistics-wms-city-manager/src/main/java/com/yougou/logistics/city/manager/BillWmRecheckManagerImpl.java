package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.service.BillWmRecheckService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@Service("billWmRecheckManager")
class BillWmRecheckManagerImpl extends BaseCrudManagerImpl implements BillWmRecheckManager {
    @Resource
    private BillWmRecheckService billWmRecheckService;

    @Override
    public BaseCrudService init() {
        return billWmRecheckService;
    }
    
	@Override
	public int countLabelNoByRecheckNo(BillWmRecheck cc, AuthorityParams authorityParams) throws ManagerException {
		
		try {
			return billWmRecheckService.countLabelNoByRecheckNo(cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillWmRecheck> findLabelNoByRecheckNoPage(SimplePage page, BillWmRecheck cc, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecheckService.findLabelNoByRecheckNoPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Supplier> querySupplier(String locno) throws ManagerException {
		try {
			return billWmRecheckService.querySupplier(locno);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillWmOutstockDtl> queryRecheckItem(Map<String, Object> params) throws ManagerException {
		try {
			return billWmRecheckService.queryRecheckItem(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void packageBox(List<BillWmOutstockDtlDto> dtlLst, String boxNo,
			String recheckNo, String locno, String supplierNo, String userName,String userChName,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			 billWmRecheckService.packageBox(dtlLst,boxNo,recheckNo,locno,supplierNo,userName,userChName,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}

	@Override
	public void check(String ids, String loginName, String checkUser) throws ManagerException {
		try {
			billWmRecheckService.check(ids,loginName,checkUser);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		
	}

	@Override
	public void deleteBillWmOutStockRecheck(List<BillWmRecheck> listWmRechecks) throws ManagerException {
		try {
			billWmRecheckService.deleteBillWmOutStockRecheck(listWmRechecks);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void packageBoxRf(BillWmRecheck billWmRecheck) throws ManagerException {
		try {
			billWmRecheckService.packageBoxRf(billWmRecheck);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
    
}