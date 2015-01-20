package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BsPrinterDockKey;
import com.yougou.logistics.city.dal.mapper.BsPrinterDockMapper;

/*
 * 请写出类的用途 
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
@Service("bsPrinterDockService")
class BsPrinterDockServiceImpl extends BaseCrudServiceImpl implements BsPrinterDockService {
	@Resource
	private BsPrinterDockMapper bsPrinterDockMapper;

	@Override
	public BaseCrudMapper init() {
		return bsPrinterDockMapper;
	}

	@Override
	public void deleteByDockNo(BsPrinterDockKey key) throws ServiceException {
		try {
			bsPrinterDockMapper.deleteByDockNo(key);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}