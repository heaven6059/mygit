package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillChPlanDtl;
import com.yougou.logistics.city.dal.mapper.BillChPlanDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
@Service("billChPlanDtlService")
class BillChPlanDtlServiceImpl extends BaseCrudServiceImpl implements BillChPlanDtlService {
    @Resource
    private BillChPlanDtlMapper billChPlanDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billChPlanDtlMapper;
    }

	@Override
	public Long findMaxId() throws ServiceException {
		try {
			return billChPlanDtlMapper.selectMaxId();
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int deleteByPlanNo(BillChPlanDtl billChPlanDtl) throws ServiceException {
		try {
			return billChPlanDtlMapper.deleteByPlanNo(billChPlanDtl);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public int deleteByBillKey(BillChPlanDtl billChPlanDtl) throws ServiceException {
		try {
			return billChPlanDtlMapper.deleteByBillKey(billChPlanDtl);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public void batchInsertDtl(List<BillChPlanDtl> list) throws ServiceException {
		try{
			billChPlanDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillChPlanDtl> findDuplicateRecord(Map<String, Object> params)
			throws ServiceException {
		try{
			return billChPlanDtlMapper.selectDuplicateRecord(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
		
	}

	@Override
	public List<Map<String,Object>> findCellNo(Map<String, Object> params)
			throws ServiceException {
		try {
			return billChPlanDtlMapper.selectCellNo(params);
		} catch (DaoException e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public Integer batchInsertPlanDtl4AllCell(Map<String, Object> params)
			throws ServiceException {
		try {
			return billChPlanDtlMapper.batchInsertPlanDtl4AllCell(params);
		} catch (DaoException e) {
			throw new ServiceException("",e);
		}
	}
	
	
}