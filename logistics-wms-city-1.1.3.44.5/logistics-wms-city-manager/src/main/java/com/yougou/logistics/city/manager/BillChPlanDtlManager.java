package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtl;

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
public interface BillChPlanDtlManager extends BaseCrudManager {
	public String save(BillChPlan billChPlan,List<BillChPlanDtl> insertedList , List<BillChPlanDtl>  deletedList) throws ManagerException;
	/**
	 * 根据品牌保存盘点计划明细
	 * @param billChPlan
	 * @return
	 * @throws ManagerException
	 */
	public String saveByBrand(BillChPlan billChPlan) throws ManagerException;
	/**
	 * 整库区或整通道添加明细
	 * @param billChPlan
	 * @param wareNo
	 * @param areaNo
	 * @param stockNo
	 * @return
	 * @throws ManagerException
	 */
	public String saveDtlBatch(BillChPlan billChPlan,String wareNo,String areaNo,String stockNo)throws ManagerException;
}