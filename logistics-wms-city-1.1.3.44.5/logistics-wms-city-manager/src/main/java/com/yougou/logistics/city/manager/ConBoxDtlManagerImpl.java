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
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.ConBoxDtlService;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
@Service("conBoxDtlManager")
class ConBoxDtlManagerImpl extends BaseCrudManagerImpl implements ConBoxDtlManager {
	@Resource
	private ConBoxDtlService conBoxDtlService;

	@Override
	public BaseCrudService init() {
		return conBoxDtlService;
	}

	@Override
	public int countBoxAndNum(ConBoxDtl cc, AuthorityParams authorityParams) throws ManagerException {

		try {
			return conBoxDtlService.countBoxAndNum(cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<ConBoxDtl> findCnBoxAndNumPage(SimplePage page, ConBoxDtl cc, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return conBoxDtlService.findCnBoxAndNumPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItem4umuntreadCount(Map<String, String> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return conBoxDtlService.selectItem4umuntreadCount(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<ConBoxDtl> selectItem4umuntread(Map<String, String> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return conBoxDtlService.selectItem4umuntread(map, page, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	@Override
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return conBoxDtlService.findSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}