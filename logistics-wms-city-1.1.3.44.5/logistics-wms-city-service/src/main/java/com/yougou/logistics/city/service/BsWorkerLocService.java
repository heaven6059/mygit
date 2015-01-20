package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefloc;

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
public interface BsWorkerLocService extends BaseCrudService {
	/**
	 * 删除用户下的所有仓库
	 * @param obj
	 * @return
	 * @throws ServiceException
	 */
	public int deleteByWorkerNo(Object obj) throws ServiceException;

	/**
	 * 通过用户编码获取仓别
	 * @param params
	 * @return
	 */
	public List<BmDefloc> findLocByWorkerNo(Map<String, Object> params) throws ServiceException;
}