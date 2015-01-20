package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.CmDefstock;
import com.yougou.logistics.city.dal.mapper.CmDefstockMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 通道service实现
 * 
 * @author qin.dy
 * @date 2013-9-26 下午4:02:49
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefstockService")
class CmDefstockServiceImpl extends BaseCrudServiceImpl implements CmDefstockService {
    @Resource
    private CmDefstockMapper cmDefstockMapper;

    @Override
    public BaseCrudMapper init() {
        return cmDefstockMapper;
    }

	@Override
	public int queryStoreNo0(CmDefstock cmDefstock) throws ServiceException {
		int count = 0;
		try {
			if(cmDefstock != null) {
				count = cmDefstockMapper.queryStoreNo0(cmDefstock);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}

	@Override
	public int queryStoreNo1(CmDefstock cmDefstock) throws ServiceException {
		int count = 0;
		try {
			if(cmDefstock != null) {
				count = cmDefstockMapper.queryStoreNo1(cmDefstock);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}

	@Override
	public int queryStoreNo2(CmDefstock cmDefstock) throws ServiceException {
		int count = 0;
		try {
			if(cmDefstock != null) {
				count = cmDefstockMapper.queryStoreNo2(cmDefstock);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		return count;
	}
}