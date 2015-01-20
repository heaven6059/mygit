package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillOmDivideDtlMapper;

@Service("billOmDivideDtlService")
class BillOmDivideDtlServiceImpl extends BaseCrudServiceImpl implements BillOmDivideDtlService {
    @Resource
    private BillOmDivideDtlMapper billOmDivideDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billOmDivideDtlMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int modifyBillOmDivideByDivideNoAndlocno(BillOmDivide divide)
			throws ServiceException {
		try {
			int count = 0;
			List<BillOmDivideDtl> listBillOmDivide = divide.getListBillOmDivideDtl();
			if(CommonUtil.hasValue(listBillOmDivide)){
				for (BillOmDivideDtl d : listBillOmDivide) {
					BillOmDivideDtl divideDtl = new BillOmDivideDtl();
					divideDtl.setDivideNo(d.getDivideNo());
					divideDtl.setLocno(d.getLocno());
					divideDtl.setAssignName(divide.getAssignNames());
					divideDtl.setAssignNameCh(divide.getAssignNamesCh());
					count += billOmDivideDtlMapper.updateBillOmDivideByDivideNoAndlocno(divideDtl);
				}
			}
			return count;
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
    
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmDivideDtlMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmDivideDtl> findDivideDtlNotRecheckBox(Map<String, Object> params) throws ServiceException {
		try {
			return billOmDivideDtlMapper.selectDivideDtlNotRecheckBox(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmDivideDtl> findDivideDtl4Different(Map<String, Object> params) throws ServiceException {
		try {
			return billOmDivideDtlMapper.selectDivideDtl4Different(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateDivideDtl4Different(BillOmDivideDtl divideDtl) throws ServiceException {
		try {
			return billOmDivideDtlMapper.updateDivideDtl4Different(divideDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public BillOmDivideDtl selectDivideDtlByDifferent(BillOmDivideDtl divideDtl) throws ServiceException {
		try {
			return billOmDivideDtlMapper.selectDivideDtlByDifferent(divideDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Long selectDivideId() throws ServiceException {
		try {
			return billOmDivideDtlMapper.selectDivideId();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}