package com.yougou.logistics.city.dal.database;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface BmCircleMapper extends BaseCrudMapper {
	public int deleteCircle(String circleNo) throws DaoException;
}