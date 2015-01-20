package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BmDefloc;
import com.yougou.logistics.city.dal.database.BsWorkerLocMapper;

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
@Service("bsWorkerLocService")
class BsWorkerLocServiceImpl extends BaseCrudServiceImpl implements BsWorkerLocService {
	@Resource
	private BsWorkerLocMapper bsWorkerLocMapper;

	@Override
	public BaseCrudMapper init() {
		return bsWorkerLocMapper;
	}

	@Override
	public int deleteByWorkerNo(Object obj) throws ServiceException {
		try {
			return bsWorkerLocMapper.deleteByWorkerNo(obj);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public List<BmDefloc> findLocByWorkerNo(Map<String, Object> params) throws ServiceException {
		try {
			return bsWorkerLocMapper.selectLocByWorkerNo(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}