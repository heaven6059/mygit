/*
 * 类名 com.yougou.logistics.city.dal.database.BillHmPlanMapper
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillHmPlan;

public interface BillHmPlanMapper extends BaseCrudMapper {
	
	public List<BillHmPlan> selectHmPlanBySourceNo(BillHmPlan billHmPlan) throws DaoException;
	
	public void procHmIsallowmovestock(Map<String, String> maps) throws DaoException;
	
	/**
	 * 验证是否存在发单数据或者回单数据
	 * @param billHmPlan
	 * @return
	 * @throws DaoException
	 */
	public int checkIsCancelHmPlan(BillHmPlan billHmPlan) throws DaoException;
}