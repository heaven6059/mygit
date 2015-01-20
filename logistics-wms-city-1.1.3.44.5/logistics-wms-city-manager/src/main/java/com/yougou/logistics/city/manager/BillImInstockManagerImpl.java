package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.service.BillImInstockService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 09:51:28 CST 2013
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
@Service("billImInstockManager")
class BillImInstockManagerImpl extends BaseCrudManagerImpl implements BillImInstockManager {
	@Resource
	private BillImInstockService billImInstockService;

	@Override
	public BaseCrudService init() {
		return billImInstockService;
	}

	@Override
	public Map<String, Object> audit(BillImInstock instock) throws ManagerException {
		try {
			return billImInstockService.audit(instock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

}