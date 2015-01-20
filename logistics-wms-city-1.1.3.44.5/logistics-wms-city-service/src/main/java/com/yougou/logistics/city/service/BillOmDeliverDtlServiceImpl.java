package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillOmDeliverDtlMapper;

/**
 * 
 * 装车单详情service实现
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:28:10
 * @version 0.1.0 
 * @copyright yougou.com
 */   
@Service("billOmDeliverDtlService")
class BillOmDeliverDtlServiceImpl extends BaseCrudServiceImpl implements BillOmDeliverDtlService {
    @Resource  
    private BillOmDeliverDtlMapper billOmDeliverDtlMapper;

    @Override   
    public BaseCrudMapper init() {
        return billOmDeliverDtlMapper;
    }
    
    /**
     * 最大ID
     */
    @Override
	public int selectMaxNum(BillOmDeliverDtlKey keyObj) throws ServiceException {
		return billOmDeliverDtlMapper.selectMaxNum(keyObj);
	}
    
    /**
     * 检查重复
     */
    @Override
	public int selectDeliverDtl(BillOmDeliverDtlKey keyObj)
			throws ServiceException {
		return billOmDeliverDtlMapper.selectDeliverDtl(keyObj);
	}
    
    /**
     * 删除明细
     */
    @Override
	public int deleteByPrimaryKey(BillOmDeliverDtlKey keyObj) throws ServiceException {
		return billOmDeliverDtlMapper.deleteByPrimaryKey(keyObj);
		
	}
    
   /**
    * 查询复核明细
    */
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> selectBillOmRecheckDtl(
			Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.selectRecheckDetail(params, authorityParams);
	}
    
    /**
     * 查询派车明细
     */
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> selectLoadproposeDetail(
			Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.selectLoadproposeDetail(params, authorityParams);
	}

    /**
     * 复核
     */
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int boxSelectCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		return billOmDeliverDtlMapper.boxSelectCount(params, authorityParams);
	}
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> boxSelectQuery(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.boxSelectQuery(page, orderByField, orderBy,params,authorityParams);
	}
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> boxDtlByParams(BillOmDeliverDtl modelType, Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		return billOmDeliverDtlMapper.boxDtlByParams(modelType, params, authorityParams);
	}
    
    /**
	 * 派车
	 */
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int flagSelectCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		return billOmDeliverDtlMapper.loadBoxSelectCount(params, authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> flagSelectQuery(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.loadBoxSelectQuery(page, orderByField, orderBy,params,authorityParams);
	}
	
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> flagDtlByParams(BillOmDeliverDtl modelType, Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		return billOmDeliverDtlMapper.flagDtlByParams(modelType, params, authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectBoxCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmDeliverDtlMapper.selectBoxCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> selectBoxStore(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.selectBoxStore(page, orderByField, orderBy,params,authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectFlagCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmDeliverDtlMapper.selectFlagCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> selectFlagStore(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		return billOmDeliverDtlMapper.selectFlagStore(page, orderByField, orderBy,params,authorityParams);
	}
	
	@Override
	public List<BillOmDeliverDtl> findBoxNoByDetail(BillOmDeliverDtl billOmDeliverDtl) throws ServiceException {
		try {
			return billOmDeliverDtlMapper.selectBoxNoByDetail(billOmDeliverDtl);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findDeliverDtlBoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDeliverDtlMapper.selectDeliverDtlBoxCount(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> findDeliverDtlBoxByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDeliverDtlMapper.selectDeliverDtlBoxByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDeliverDtlMapper.selectSumQty(map,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findLoadproposeDeliverDtlBoxCount(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDeliverDtlMapper.selectLoadproposeDeliverDtlBoxCount(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliverDtl> findLoadproposeDeliverDtlBoxByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDeliverDtlMapper.selectLoadproposeDeliverDtlBoxByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillOmDeliverDtlSizeDto> findDtl4SizeHorizontal(String deliverNo) {
		return billOmDeliverDtlMapper.selectDtl4SizeHorizontal(deliverNo);
	}
	
}