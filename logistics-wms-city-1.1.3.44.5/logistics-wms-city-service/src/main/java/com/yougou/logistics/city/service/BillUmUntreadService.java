package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillUmUntreadService extends BaseCrudService {
	public void saveMain(BillUmUntread untreadMm, SystemUser user) throws ServiceException;

	public void deleteUntread(String keyStr, String locnoNo)
			throws ServiceException;

	public void auditUntread(String keyStr, SystemUser user)
			throws ServiceException;

	public List<BillUmUntread> findUntread2CheckTask(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			List<BillUmUntread> list, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 批量更新退仓单状态
	 * 
	 * @param params
	 *            key:status
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @author wanghb
	 */
	public int batchUpdateUntreadStatus(Map<String, Object> params,
			List<BillUmUntread> list) throws ServiceException;

	/**
	 * 退仓单合计
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException;

	public int judgeObjIsExist(BillUmUntreadKey billUmUntreadKey, String status)
			throws ServiceException;
}