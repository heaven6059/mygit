package com.yougou.logistics.city.dal.mapper;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.CmDefstock;

/**
 * 
 * 通道mapper
 * 
 * @author qin.dy
 * @date 2013-9-26 下午3:59:32
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefstockMapper extends BaseCrudMapper {

	public int queryStoreNo0(CmDefstock cmDefstock)throws DaoException;
	
	public int queryStoreNo1(CmDefstock cmDefstock)throws DaoException;
	
	public int queryStoreNo2(CmDefstock cmDefstock)throws DaoException;
	
}