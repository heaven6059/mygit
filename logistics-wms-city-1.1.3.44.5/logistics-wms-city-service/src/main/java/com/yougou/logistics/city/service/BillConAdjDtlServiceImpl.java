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
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.BillConAdjDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillConAdjDtlMapper;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
@Service("billConAdjDtlService")
class BillConAdjDtlServiceImpl extends BaseCrudServiceImpl implements BillConAdjDtlService {
    
    @Resource
    private BillConAdjDtlMapper billConAdjDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billConAdjDtlMapper;
    }
    
    @Override
	public int checkAty(Map<String, Object> map) throws ServiceException {
		return billConAdjDtlMapper.checkAty(map);
	}
    
    @Override
	public int selectMaxRowId(BillConAdjDtl model) {
		return billConAdjDtlMapper.selectMaxRowId(model);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillConAdjDtl> selectItem(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectItem(page, orderByField, orderBy,params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectItemCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billConAdjDtlMapper.selectItemCount(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int selectCellId(Map<String, Object> map) throws ServiceException {
		return billConAdjDtlMapper.checkItem(map);
	}
	
	@Override
	public List<BillConAdjDtl> selectContentParams(BillConAdjDtl modelType,
			Map<String, Object> params) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectContentParams(modelType, params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public List<String> findDtlCell(Map<String, Object> params) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectDtlCell(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public List<String> findDtlCon(Map<String, Object> params) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectDtlCon(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public List<String> findConIdFromDtl(Map<String, Object> params) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectConIdFromDtl(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billConAdjDtlMapper.selectSumQty(map,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public void batchInsertDtl(List<BillConAdjDtl> list)
			throws ServiceException {
		try{
			billConAdjDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public void batchInsertDtl4ConvertGoods(List<BillConAdjDtl> list) throws ServiceException {
		try{
			billConAdjDtlMapper.batchInsertDtl4ConvertGoods(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillConAdjDtlSizeDto> findAllDtl(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException {
        try {
        	return this.billConAdjDtlMapper.findAllDtl(params, authorityParams);
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }

	@Override
	public List<BillConAdjDtl> findCheckRepeatData(Map<String, Object> params) throws ServiceException {
		try{
			return billConAdjDtlMapper.selectCheckRepeatData(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	public List<BillConAdjDtl> selectListByConNo(Map<String,Object> params)throws ServiceException{
		try{
			return billConAdjDtlMapper.selectListByConNo(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	public int deleteByConNo(Map<String, Object> tempQueryParam) throws ServiceException {
		try {
			return billConAdjDtlMapper.deleteByConNo(tempQueryParam);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public void updateOperateRecord(Map<String, Object> map)
			throws ServiceException {
		try {
			billConAdjDtlMapper.updateOperateRecord(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
}