package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.LookupMapper;

@Service("lookupService")
class LookupServiceImpl extends BaseCrudServiceImpl implements LookupService {
    @Resource
    private LookupMapper lookupMapper;

    @Override
    public BaseCrudMapper init() {
        return lookupMapper;
    }

	@Override
	public int checkItemValue(String itemval, String lookupcode, int systemid) throws ServiceException {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("itemval", itemval);
		map.put("lookupcode", lookupcode);
		map.put("systemid", systemid);
		return lookupMapper.checkItemValue(map);
	}

	@Override
	public void deletelookup(String lookupcode) throws ServiceException {
		lookupMapper.deletelookup(lookupcode);
		lookupMapper.deletelookupdtl(lookupcode);
	}

	@Override
	public int checkLookuoCode(String lookupcode) throws ServiceException {
		return lookupMapper.checkLookuoCode(lookupcode);
	}
}