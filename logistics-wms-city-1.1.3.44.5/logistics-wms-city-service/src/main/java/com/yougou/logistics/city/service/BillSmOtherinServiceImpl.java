package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillSmOtherinKey;
import com.yougou.logistics.city.dal.mapper.BillSmOtherinMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@Service("billSmOtherinService")
class BillSmOtherinServiceImpl extends BaseCrudServiceImpl implements BillSmOtherinService {
    @Resource
    private BillSmOtherinMapper billSmOtherinMapper;

    @Override
    public BaseCrudMapper init() {
        return billSmOtherinMapper;
    }
    
	@Override
	public int deleteDtlById(BillSmOtherinKey key) throws ServiceException {
		try {
			return billSmOtherinMapper.deleteDtlById(key);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billSmOtherinMapper.selectSumQty(params, authorityParams);
	}
}