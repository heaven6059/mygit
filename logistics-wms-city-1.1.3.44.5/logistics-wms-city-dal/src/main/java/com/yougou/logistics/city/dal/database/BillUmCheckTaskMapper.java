/*
 * 类名 com.yougou.logistics.city.dal.database.BillUmCheckTaskMapper
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmCheckTask;

public interface BillUmCheckTaskMapper extends BaseCrudMapper {
	/**
	 * 回滚店退仓状态为-11已审核
	 * @param taskList
	 * @return
	 * @throws DaoException
	 */
	public int updateRollbackUntreadStatus4CheckTask(@Param("params") BillUmCheckTask umCheckTask,
			@Param("list") List<BillUmCheckTask> taskList) throws DaoException;

	/**
	 * 退仓验收任务合计
	 * @param params
	 * @param authorityParams
	 * @return
	 */
    public Map<String, Object> selectSumQty(@Param("params")Map<String, Object> params, 
            @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}