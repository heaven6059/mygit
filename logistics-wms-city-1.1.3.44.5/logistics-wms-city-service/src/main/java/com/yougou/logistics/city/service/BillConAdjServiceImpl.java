package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.dal.mapper.BillConAdjMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

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
@Service("billConAdjService")
class BillConAdjServiceImpl extends BaseCrudServiceImpl implements BillConAdjService {
    @Resource
    private BillConAdjMapper billConAdjMapper;

    @Override
    public BaseCrudMapper init() {
        return billConAdjMapper;
    }
    
    @Override
	public void deleteStockAdjDetail(BillConAdjDtl model) {
		billConAdjMapper.deleteStockAdjDetail(model);
	}

	@Override
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return this.billConAdjMapper.selectSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
    
}