package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.Lookupdtl;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.dal.database.LookupdtlMapper;
import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service("lookupdtlService")
class LookupdtlServiceImpl extends BaseCrudServiceImpl implements LookupdtlService {
    @Resource
    private LookupdtlMapper lookupdtlMapper;

    @Override
    public BaseCrudMapper init() {
        return lookupdtlMapper;
    }

	@Override
	public List<Lookupdtl> selectOutstockDirectExpType(Lookupdtl lookupDtl)
			throws ServiceException {
		try{
			return  lookupdtlMapper.selectOutstockDirectExpType(lookupDtl);
		}catch(Exception e){
			throw new  ServiceException(e);
			
		}
	}

	@Override
	public List<Lookupdtl> selectLookupdtlBySysNo(Lookupdtl lookupDtl)
			throws ServiceException {
		try{
			return  lookupdtlMapper.selectLookupdtlBySysNo(lookupDtl);
		}catch(Exception e){
			throw new  ServiceException(e);
			
		}
	}

	@Override
	public List<Lookupdtl> selectLookupdtlByCode(Map<String, Object> params)
			throws ServiceException {
		try{
			return  lookupdtlMapper.selectLookupdtlByCode(params);
		}catch(Exception e){
			throw new  ServiceException(e);
			
		}
	}
	
	
	
}