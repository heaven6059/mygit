package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
public interface BillWmPlanService extends BaseCrudService {
    public void saveMain(BillWmPlan billWmPlan) throws ServiceException;

    public void deletePlan(String keyStr, String locnoNo)
	    throws ServiceException;

    public void auditPlan(String keyStr, String locno, String oper,String userName)
	    throws ServiceException;
    
    /**
     * su.yq
     * 转库存锁定
     * @param list
     * @throws ServiceException
     */
	public void toStoreLock(List<BillWmPlan> list, SystemUser user) throws ServiceException;
	
	/**
	 * 转退厂申请
	 * @param billWmPlanKey
	 * @param userName
	 *  @param chUserName 中文名称
	 * @throws ServiceException
	 */
	public void changeWMRequest(BillWmPlanKey billWmPlanKey,String userName,String chUserName) throws ServiceException;
	/**
	 * 转移库计划
	 * @param billWmPlanKey
	 * @param userName
	 * @throws ServiceException
	 */
	public void changeHMPlan(BillWmPlanKey billWmPlanKey,String userName,String chUserName) throws ServiceException;
}