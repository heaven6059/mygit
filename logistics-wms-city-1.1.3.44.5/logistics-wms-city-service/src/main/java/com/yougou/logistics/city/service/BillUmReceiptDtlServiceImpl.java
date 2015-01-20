package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmReceiptDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmReceiptDtlService")
class BillUmReceiptDtlServiceImpl extends BaseCrudServiceImpl implements BillUmReceiptDtlService {
    @Resource
    private BillUmReceiptDtlMapper billUmReceiptDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billUmReceiptDtlMapper;
    }

	@Override
	public int findCountByBox(Map<String, Object> params) throws ServiceException {
		try {
			return billUmReceiptDtlMapper.selectCountByBox(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillUmReceiptDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ServiceException {
		try {
			return this.billUmReceiptDtlMapper.selectByPageByBox(page, params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmReceiptDtlMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}