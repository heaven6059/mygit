package com.yougou.logistics.city.dal.database;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccTaskDtl;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-24 13:44:52
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
public interface AccTaskDtlMapper extends BaseCrudMapper {
	/**
	 * 查询明细通过rowId
	 * @param list
	 * @throws DaoException
	 */
	public AccTaskDtl selectByPrimaryRowId(AccTaskDtl accTaskDtl) throws DaoException;
}