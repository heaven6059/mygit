/*
 * 类名 com.yougou.logistics.city.dal.mapper.BsPrinterDockMapper
 * @author luo.hl
 * @date  Fri Nov 01 15:18:23 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BsPrinterDockKey;

public interface BsPrinterDockMapper extends BaseCrudMapper {
	public void deleteByDockNo(BsPrinterDockKey key) throws DaoException;
}