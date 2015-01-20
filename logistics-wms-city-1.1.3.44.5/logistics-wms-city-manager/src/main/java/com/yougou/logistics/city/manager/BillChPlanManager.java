package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
public interface BillChPlanManager extends BaseCrudManager {
	public String check(BillChPlan billChPlan,String planNos) throws ManagerException;
	public String invalid(BillChPlan billChPlan, String[] planNos, SystemUser user) throws ManagerException;
	public String deleteMain(BillChPlan billChPlan,String planNos) throws ManagerException;
	public Object saveMain(BillChPlan billChPlan, AuthorityParams authorityParams) throws ManagerException;
	public void editMainInfo(BillChPlan billChPlan, AuthorityParams authorityParams) throws ManagerException;
}