package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;
import com.yougou.logistics.city.dal.mapper.BillImDifRecordDtlMapper;
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
@Service("billImDifRecordDtlService")
class BillImDifRecordDtlServiceImpl extends BaseCrudServiceImpl implements BillImDifRecordDtlService {
    @Resource
    private BillImDifRecordDtlMapper billImDifRecordDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billImDifRecordDtlMapper;
    }

    @Override
	public int selectContentCount(Map<String, Object> params)
			throws ServiceException {
		try {
			return billImDifRecordDtlMapper.selectContentCount(params,null);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillImDifRecordDtl> selectContent(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params) throws ServiceException {
		try {
			return billImDifRecordDtlMapper.selectContent(page, orderByField, orderBy, params, null);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int selectMaxPid(BillImDifRecordDtl billImDifRecordDtl)
			throws ServiceException {
		try {
			return billImDifRecordDtlMapper.selectMaxPid(billImDifRecordDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	
}