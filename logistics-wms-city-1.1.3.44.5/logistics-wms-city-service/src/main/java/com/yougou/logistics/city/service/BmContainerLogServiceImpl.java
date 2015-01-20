package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BmContainerLog;
import com.yougou.logistics.city.dal.database.BmContainerLogMapper;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * 容器操作日志
 * @author wanghb
 * @date   2014-8-16
 * @version 1.1.3.39
 */
@Service("bmContainerLogService")
class BmContainerLogServiceImpl extends BaseCrudServiceImpl implements BmContainerLogService {
    @Resource
    private BmContainerLogMapper bmContainerLogMapper;

    @Override
    public BaseCrudMapper init() {
        return bmContainerLogMapper;
    }
    public Integer findDtlCount(Map<String, Object> params) throws ServiceException{
    	try {
			return bmContainerLogMapper.findDtlCount(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
    }
    public List<BmContainerLog> findDtlByPage(SimplePage page,String sortColumn,String sortOrder,Map<String, Object> params)throws ServiceException{
    	try {
			return bmContainerLogMapper.findDtlByPage(page, sortColumn, sortOrder, params,null);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
    }

}