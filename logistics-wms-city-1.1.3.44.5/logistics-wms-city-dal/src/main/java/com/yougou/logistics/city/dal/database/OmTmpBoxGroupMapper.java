/*
 * 类名 com.yougou.logistics.city.dal.database.OmTmpBoxGroupMapper
 * @author su.yq
 * @date  Wed Dec 04 14:47:49 CST 2013
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

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface OmTmpBoxGroupMapper extends BaseCrudMapper {
	
	public String getNextvalId() throws DaoException;
}