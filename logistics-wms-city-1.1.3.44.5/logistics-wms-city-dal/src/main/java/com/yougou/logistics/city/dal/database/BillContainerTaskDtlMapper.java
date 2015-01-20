package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
public interface BillContainerTaskDtlMapper extends BaseCrudMapper {

	/**
	 * 批量新增容器任务明细
	 * @param taskDtlList
	 * @throws DaoException
	 */
	public void batchInsertDtl(List<BillContainerTaskDtl> taskDtlList) throws DaoException;

}