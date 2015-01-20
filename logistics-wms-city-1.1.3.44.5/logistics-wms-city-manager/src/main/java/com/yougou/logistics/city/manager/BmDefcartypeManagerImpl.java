package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefcartype;
import com.yougou.logistics.city.service.BmDefcartypeService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 09:27:35 CST 2013
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
@Service("bmDefcartypeManager")
class BmDefcartypeManagerImpl extends BaseCrudManagerImpl implements BmDefcartypeManager {
	@Resource
	private BmDefcartypeService bmDefcartypeService;

	@Override
	public BaseCrudService init() {
		return bmDefcartypeService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBmDefcartypeBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BmDefcartype type = null;
			for (String obj : strs) {
				try {
					type = new BmDefcartype();
					type.setCartypeNo(obj);
					count += bmDefcartypeService.deleteById(type);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}
}