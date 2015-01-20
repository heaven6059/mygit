package com.yougou.logistics.city.dal.database;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface BmDeflocMapper extends BaseCrudMapper {
	
	public int deleteFefloc(String locNo) throws DaoException;
	
}