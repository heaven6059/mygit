package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillImReceiptDtlMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@Service("billOmPrepareDivideDtlService")
class BillOmPrepareDivideDtlServiceImpl extends BaseCrudServiceImpl implements BillOmPrepareDivideDtlService {
	@Resource
	private BillImReceiptDtlMapper billImReceiptDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billImReceiptDtlMapper;
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findPrepareDivideDetailCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectPrepareDivideDetailCount(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceiptDtl> findPrepareDivideDetail(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectPrepareDivideDetail(page, map,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	public int findDtlByItemNoAndSizeNo(BillImReceiptDtl dtl) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDtlByItemNoAndSizeNo(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceiptDtl> selectItemDetail4Prepare(Map<String, Object> map, SimplePage page,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetail4Prepare(map, page,authorityParams);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectItemDetail4PrepareCount(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetail4PrepareCount(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) {
		return this.billImReceiptDtlMapper.selectSumQty(map,authorityParams);
	}


	@Override
	public List<BillImReceiptDtl> selectBoxPan4ReceiptDtl(Map<String, Object> map) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectBoxPan4ReceiptDtl(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectPanIsExist(BillImReceiptDtl receiptDtl) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectPanIsExist(receiptDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer batchUpdateBoxStatus4Prepare(Map<String, Object> map) throws ServiceException {
		try {
			return billImReceiptDtlMapper.batchUpdateBoxStatus4Prepare(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}