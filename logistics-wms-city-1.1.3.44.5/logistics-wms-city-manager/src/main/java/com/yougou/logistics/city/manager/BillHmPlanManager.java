package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillHmPlanManager extends BaseCrudManager {

	public String saveMain(BillHmPlan billHmPlan,SystemUser user) throws ManagerException;

	/**
	 * 新增移仓出库单
	 * @param billHmPlan
	 * @return
	 * @throws ManagerException
	 */
	public <ModelType> BillHmPlan addBillHmPlan(BillHmPlan billHmPlan, Map<CommonOperatorEnum, List<ModelType>> params,SystemUser user)
			throws ManagerException;

	/**
	 * 删除移仓出库单
	 * @param listBillHmPlan
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteBillHmPlan(List<BillHmPlan> listBillHmPlan) throws ManagerException;

	/**
	 * 审核
	 * @param planNo
	 * @param locoNo
	 * @param oper TODO
	 * @throws ManagerException
	 */
	public void audit(String planNo, SystemUser user) throws ManagerException;
	
	/**
	 * 作废移库单
	 * @param billHmPlan
	 * @throws ServiceException
	 */
	public void cancelBillHmPlan(List<BillHmPlan> listPlans, SystemUser user) throws ManagerException;
}