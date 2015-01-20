package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillChCheckDirectBox;
import com.yougou.logistics.city.dal.database.BillChCheckDirectBoxMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-10-21 09:51:39
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
@Service("billChCheckDirectBoxService")
class BillChCheckDirectBoxServiceImpl extends BaseCrudServiceImpl implements BillChCheckDirectBoxService {
    @Resource
    private BillChCheckDirectBoxMapper billChCheckDirectBoxMapper;

    @Override
    public BaseCrudMapper init() {
        return billChCheckDirectBoxMapper;
    }

	@Override
	public void insert4Bath(List<BillChCheckDirectBox> list)throws ServiceException{
		try{
			billChCheckDirectBoxMapper.insert4Bath(list);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}
}