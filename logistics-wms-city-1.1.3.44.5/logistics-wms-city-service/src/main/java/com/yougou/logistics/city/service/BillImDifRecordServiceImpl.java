package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillImDifRecordKey;
import com.yougou.logistics.city.dal.mapper.BillImDifRecordMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
@Service("billImDifRecordService")
class BillImDifRecordServiceImpl extends BaseCrudServiceImpl implements BillImDifRecordService {
    @Resource
    private BillImDifRecordMapper billImDifRecordMapper;

    @Override
    public BaseCrudMapper init() {
        return billImDifRecordMapper;
    }

	@Override
	public int deleteDtlById(BillImDifRecordKey key) throws ServiceException {
		try {
			return billImDifRecordMapper.deleteDtlById(key);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

}