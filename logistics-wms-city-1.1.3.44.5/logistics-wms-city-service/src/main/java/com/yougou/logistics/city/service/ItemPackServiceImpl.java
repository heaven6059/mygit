package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.ItemPackMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Oct 09 19:26:37 CST 2013
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
@Service("itemPackService")
class ItemPackServiceImpl extends BaseCrudServiceImpl implements ItemPackService {
	@Resource
	private ItemPackMapper itemPackMapper;

	@Override
	public BaseCrudMapper init() {
		return itemPackMapper;
	}

	@Override
	public List<Map<String, String>> selectPackSpec() throws ServiceException {
		try {
			return itemPackMapper.selectPackSpec();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}