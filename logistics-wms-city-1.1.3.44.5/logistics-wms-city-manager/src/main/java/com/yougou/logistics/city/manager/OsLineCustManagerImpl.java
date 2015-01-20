package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.OsLineCust;
import com.yougou.logistics.city.service.OsLineCustService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Sep 26 12:33:06 CST 2013
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
@Service("osLineCustManager")
class OsLineCustManagerImpl extends BaseCrudManagerImpl implements OsLineCustManager {
	@Resource
	private OsLineCustService osLineCustService;

	@Override
	public BaseCrudService init() {
		return osLineCustService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteOsLineCustBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			OsLineCust cust = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					cust = new OsLineCust();
					cust.setLocno(substr[0]);
					cust.setStoreNo(substr[1]);
					cust.setLineNo(substr[2]);
					count += osLineCustService.deleteById(cust);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

}