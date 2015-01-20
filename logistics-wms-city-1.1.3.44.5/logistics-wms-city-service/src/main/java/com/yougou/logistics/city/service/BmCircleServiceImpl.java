package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BmCircleMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bmCircleService")
class BmCircleServiceImpl extends BaseCrudServiceImpl implements BmCircleService {
    @Resource
    private BmCircleMapper bmCircleMapper;

    @Override
    public BaseCrudMapper init() {
        return bmCircleMapper;
    }

	@Override
	public int deleteCircle(String circleNo) throws ServiceException {
		int  a  = 0 ;
		try{
			a = bmCircleMapper.deleteCircle(circleNo);
		}catch (Exception e) {
            throw new ServiceException(e);
        }
		return  a;
	}
}