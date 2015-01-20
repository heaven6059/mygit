package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
public interface BillHmPlanService extends BaseCrudService {
	/**
	 * 审核
	 * @param planNo
	 * @param locno
	 * @param oper TODO
	 * @throws ServiceException
	 */
	public void audit(String planNo, SystemUser user) throws ServiceException;
	
	/**
	 * 验证储位存储过程
	 * @param maps
	 * @throws DaoException
	 */
	public void procHmIsallowmovestock(Map<String, String> maps) throws ServiceException;
	
	/**
	 * 作废移库单
	 * @param billHmPlan
	 * @throws ServiceException
	 */
	public void cancelBillHmPlan(List<BillHmPlan> listPlans, SystemUser user) throws ServiceException;
}