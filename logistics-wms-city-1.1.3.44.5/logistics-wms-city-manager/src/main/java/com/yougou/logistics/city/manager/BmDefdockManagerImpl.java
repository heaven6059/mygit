package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefdock;
import com.yougou.logistics.city.service.BmDefdockService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 23 10:24:36 CST 2013
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
@Service("bmDefdockManager")
class BmDefdockManagerImpl extends BaseCrudManagerImpl implements BmDefdockManager {
	@Resource
	private BmDefdockService bmDefdockService;

	@Override
	public BaseCrudService init() {
		return bmDefdockService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBmDefdockBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BmDefdock dock = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					dock = new BmDefdock();
					dock.setLocno(substr[0]);
					dock.setDockNo(substr[1]);
					count += bmDefdockService.deleteById(dock);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}
}