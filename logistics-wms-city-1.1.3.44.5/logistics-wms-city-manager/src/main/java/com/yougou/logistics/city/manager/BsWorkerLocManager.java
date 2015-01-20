package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BsWorkerLocManager extends BaseCrudManager {

	/**
	 * 保存用户和仓别信息
	 * @return
	 * @throws ManagerException
	 */
	public int saveWorkerLoc(String parmStr, String creator) throws ManagerException;

	/**
	 * 通过用户编码获取仓别
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BmDefloc> findLocByWorkerNo(Map<String, Object> params) throws ManagerException;
}