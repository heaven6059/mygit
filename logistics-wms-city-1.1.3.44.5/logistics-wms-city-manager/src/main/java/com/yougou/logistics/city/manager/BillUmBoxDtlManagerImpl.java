package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmBoxDtl;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmBoxDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:21:11 CST 2014
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
@Service("billUmBoxDtlManager")
class BillUmBoxDtlManagerImpl extends BaseCrudManagerImpl implements BillUmBoxDtlManager {
	@Resource
	private BillUmBoxDtlService billUmBoxDtlService;

	@Override
	public BaseCrudService init() {
		return billUmBoxDtlService;
	}

	public void createBoxDtl(BillUmLoadbox box, String keyStr, SystemUser user) throws ManagerException {
		try {
			billUmBoxDtlService.createBoxDtl(box, keyStr, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billUmBoxDtlService.selectSumQty(map, authorityParams);
	}

	public void createRfNoSealed(BillUmBoxDtl box, SystemUser user) throws ManagerException {

		try {
			billUmBoxDtlService.createRfNoSealed(box, user);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}