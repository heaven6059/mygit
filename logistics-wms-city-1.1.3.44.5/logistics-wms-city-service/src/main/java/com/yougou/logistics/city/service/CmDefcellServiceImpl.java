package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;

/**
 * 
 * 储位service实现
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:23:41
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefcellService")
class CmDefcellServiceImpl extends BaseCrudServiceImpl implements CmDefcellService {

	@Resource
	private CmDefcellMapper cmDefcellMapper;

	@Override
	public BaseCrudMapper init() {
		return cmDefcellMapper;
	}

	@Override
	public List<CmDefcell> findCmDefcellByArea(CmDefcell defcell) throws ServiceException {
		try {
			return cmDefcellMapper.selectCmDefcellByArea(defcell);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<CmDefcell> findCmDefcell4Adj(SimplePage page,Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return cmDefcellMapper.selectCmDefcell4Adj(page,params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findCmDefcell4AdjCount(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException {
		int count = 0;
		try {
			count = cmDefcellMapper.selectCmDefcell4AdjCount(params,authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}
	
	@Override
	public List<CmDefcell> findCmDefcell4Plan(SimplePage page,Map<String,Object> params) throws ServiceException {
		try {
			return cmDefcellMapper.selectCmDefcell4Plan(page,params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int findCmDefcell4PlanCount(Map<String,Object> params) throws ServiceException {
		int count = 0;
		try {
			count = cmDefcellMapper.selectCmDefcell4PlanCount(params);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}
	
	@Override
	public int queryContent(CmDefcell defcell) throws ServiceException {
		int count = 0;
		try {
			if (defcell != null) {
				count = cmDefcellMapper.queryContent(defcell);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}
	
	

	public CmDefcell selectCellNo4BillHmPlan(CmDefcell defcell) throws ServiceException {
		try {
			return cmDefcellMapper.selectCellNo4BillHmPlan(defcell);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<CmDefcell> find4ReturnedGoods(Map<String,Object> params) throws ServiceException {
		try {
			return cmDefcellMapper.select4ReturnedGoods(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String findOwnerByStock(CmDefcell cmDefcell) throws ServiceException {
		try {
			return cmDefcellMapper.selectOwnerByStock(cmDefcell);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<CmDefcellSimple> findSimple(Map<String,Object> params) throws ServiceException {
		try {
			return cmDefcellMapper.selectSimple(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<CmDefcell> findDestCell4Convert(Map<String, Object> params)
			throws ServiceException {
		try {
			return cmDefcellMapper.selectDestCell4Convert(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<CmDefcell> selectCellNoByUserType(Map<String, Object> params)
			throws ServiceException {
		try {
			return cmDefcellMapper.selectCellNoByUserType(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}