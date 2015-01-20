package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmDirectMapper;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Service("billUmDirectService")
class BillUmDirectServiceImpl extends BaseCrudServiceImpl implements
	BillUmDirectService {
    @Resource
    private BillUmDirectMapper billUmDirectMapper;

    @Override
    public BaseCrudMapper init() {
	return billUmDirectMapper;
    }

    @Override
    public int selectCount4Direct(Map map) throws ServiceException {
	try {
	    return billUmDirectMapper.selectCount4Direct(map);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillUmDirect> selectByPage4Direct(SimplePage page, Map map)
	    throws ServiceException {
	try {
	    return billUmDirectMapper.selectByPage4Direct(page, map);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map) {
	return billUmDirectMapper.selectSumQty(map);
    }

}