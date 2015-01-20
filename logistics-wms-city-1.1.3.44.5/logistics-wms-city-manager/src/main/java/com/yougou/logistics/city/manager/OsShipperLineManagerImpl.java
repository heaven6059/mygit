package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.OsShipperLine;
import com.yougou.logistics.city.service.OsShipperLineService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Sep 26 11:04:15 CST 2013
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
@Service("osShipperLineManager")
class OsShipperLineManagerImpl extends BaseCrudManagerImpl implements OsShipperLineManager {
	@Resource
	private OsShipperLineService osShipperLineService;

	@Override
	public BaseCrudService init() {
		return osShipperLineService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	@Override
	public int deleteOsShipperLineBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			OsShipperLine line = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					line = new OsShipperLine();
					line.setLocno(substr[0]);
					line.setLineNo(substr[1]);
					line.setShipperNo(substr[2]);
					count += osShipperLineService.deleteById(line);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

}