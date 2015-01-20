package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.ConContentHistoryMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-06-18 10:38:59
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
@Service("conContentHistoryService")
class ConContentHistoryServiceImpl extends BaseCrudServiceImpl implements ConContentHistoryService {
    @Resource
    private ConContentHistoryMapper conContentHistoryMapper;

    @Override
    public BaseCrudMapper init() {
        return conContentHistoryMapper;
    }

	@Override
	public Integer selectSumByLocno(Map<String, Object> map)
			throws ServiceException {
		try{
			return conContentHistoryMapper.selectSumByLocno(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int insertByContent(Map<String, Object> map)
			throws ServiceException {
		try{
			return conContentHistoryMapper.insertByContent(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
}