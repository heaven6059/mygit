package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BmDeflocMapper;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bmDeflocService")
class BmDeflocServiceImpl extends BaseCrudServiceImpl implements BmDeflocService {
    @Resource
    private BmDeflocMapper bmDeflocMapper;

    @Override
    public BaseCrudMapper init() {
        return bmDeflocMapper;
    }

	@Override
	public int deleteFefloc(String locno) throws ServiceException {
		int  a  = 0 ;
		try{
			a = bmDeflocMapper.deleteFefloc(locno);
		}catch (Exception e) {
            throw new ServiceException(e);
        }
		return  a;
	}
}