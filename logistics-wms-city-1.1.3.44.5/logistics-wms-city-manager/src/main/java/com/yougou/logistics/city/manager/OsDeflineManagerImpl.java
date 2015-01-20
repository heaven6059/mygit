package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.OsDefline;
import com.yougou.logistics.city.service.OsDeflineService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 14:33:44 CST 2013
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
@Service("osDeflineManager")
class OsDeflineManagerImpl extends BaseCrudManagerImpl implements OsDeflineManager {
	@Resource
	private OsDeflineService osDeflineService;

	@Override
	public BaseCrudService init() {
		return osDeflineService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteOsDeflineBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			OsDefline line = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					line = new OsDefline();
					line.setLocno(substr[0]);
					line.setLineNo(substr[1]);
					count += osDeflineService.deleteById(line);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}
}