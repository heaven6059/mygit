package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefloc;
import com.yougou.logistics.city.common.model.BsWorkerLoc;
import com.yougou.logistics.city.common.model.BsWorkerLocKey;
import com.yougou.logistics.city.service.BsWorkerLocService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 23 10:25:26 CST 2013
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
@Service("bsWorkerLocManager")
class BsWorkerLocManagerImpl extends BaseCrudManagerImpl implements BsWorkerLocManager {
	@Resource
	private BsWorkerLocService bsWorkerLocService;

	@Override
	public BaseCrudService init() {
		return bsWorkerLocService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int saveWorkerLoc(String parmStr, String creator) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(parmStr)) {
			String[] strs = parmStr.split("\\|");
			BsWorkerLoc loc = null;
			String workerNo = strs[0];
			String[] locStrs = strs[1].split(",");
			//删除该用户所属的所有仓库
			BsWorkerLocKey locKey = new BsWorkerLocKey();
			locKey.setWorkerNo(workerNo);
			try {
				bsWorkerLocService.deleteByWorkerNo(locKey);
			} catch (Exception e) {
				throw new ManagerException(e);
			}
			for (String obj : locStrs) {
				try {
					loc = new BsWorkerLoc();
					loc.setWorkerNo(workerNo);
					loc.setLocno(obj);
					loc.setCreatetm(new Date());
					loc.setCreator(creator);
					loc.setEditor(creator);
					loc.setEdittm(new Date());
					count += bsWorkerLocService.add(loc);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

	@Override
	public List<BmDefloc> findLocByWorkerNo(Map<String, Object> params) throws ManagerException {
		try {
			return bsWorkerLocService.findLocByWorkerNo(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}