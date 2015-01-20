package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.OsLineBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.service.OsLineBufferService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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
@Service("osLineBufferManager")
class OsLineBufferManagerImpl extends BaseCrudManagerImpl implements OsLineBufferManager {
	@Resource
	private OsLineBufferService osLineBufferService;

	@Override
	public BaseCrudService init() {
		return osLineBufferService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteOsLineBufferBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			OsLineBuffer cust = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					cust = new OsLineBuffer();
					cust.setLocno(substr[0]);
					cust.setLineNo(substr[1]);
					cust.setWareNo(substr[2]);
					cust.setAreaNo(substr[3]);
					cust.setStockNo(substr[4]);
					cust.setCellNo(substr[5]);
					count += osLineBufferService.deleteById(cust);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

	@Override
	public List<Store> getStoreInfo(String cellNo,String locno) throws ManagerException {
		try {
			return osLineBufferService.getStoreInfo(cellNo,locno);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}