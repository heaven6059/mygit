package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ItemPackService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Oct 09 19:26:37 CST 2013
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
@Service("itemPackManager")
class ItemPackManagerImpl extends BaseCrudManagerImpl implements ItemPackManager {
	@Resource
	private ItemPackService itemPackService;

	@Override
	public BaseCrudService init() {
		return itemPackService;
	}

	@Override
	public List<Map<String, String>> selectPackSpec() throws ManagerException {
		try {
			return itemPackService.selectPackSpec();
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}