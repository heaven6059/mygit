package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.LogisticsBaseService;

/**
 * @author wei.hj
 * create time: 2013-6-18
 */
@Service("logisticsManager")
public class LogisticsBaseManagerImpl extends BaseCrudManagerImpl implements LogisticsBaseManager{

	@Resource
	private LogisticsBaseService logisticsBaseService;
	
	@Override
	public BaseCrudService init() {
		return logisticsBaseService;
	}
	
	@Override
	public int findCount() throws ManagerException
	{
		try {
			return logisticsBaseService.findCount();
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public <ModelType> int deleteById(ModelType modelType) throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <ModelType> int add(ModelType modelType) throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public <ModelType> int modifyById(ModelType modelType) throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int findCount(Map<String, Object> params) throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <ModelType> List<ModelType> findByPage(SimplePage page, String orderByField, String orderBy, Map<String, Object> params)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <ModelType> int save(Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

}
