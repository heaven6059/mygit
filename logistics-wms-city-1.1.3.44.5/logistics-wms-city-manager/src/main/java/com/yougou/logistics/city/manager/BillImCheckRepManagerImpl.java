package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillImCheckRepService;

/**
 * 入库查询manager实现
 * @author chen.yl1
 *
 */
@Service("billImCheckRepManager")
class BillImCheckRepManagerImpl extends BaseCrudManagerImpl implements BillImCheckRepManager {
	@Resource
	private BillImCheckRepService billImCheckRepService;

	@Override
	public BaseCrudService init() {
		return  billImCheckRepService;
	}

	@Override
	public List<SizeInfo> getSizeCodeByGroup(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepService.getSizeCodeByGroup(model, authorityParams);
	}

	@Override
	public List<String> selectAllDtlSizeKind(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepService.selectAllDtlSizeKind(model, authorityParams);
	}

	@Override
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckRepService.getCount(model, authorityParams);
	}
	
	@Override
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckRepService.getBillImCheckByGroup(model, authorityParams);
	}

	@Override
	public List<BillCheckImRep> getBillImCheckDtlIm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepService.getBillImCheckDtlIm(model, authorityParams);
	}
	
	@Override
	public List<BillCheckImRep> getBillImCheckDtlUm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepService.getBillImCheckDtlUm(model, authorityParams);
	}

	@Override
	public List<BillCheckImRep> getBillImCheckDtlOtm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepService.getBillImCheckDtlOtm(model, authorityParams);
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(BillCheckImRep model, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImCheckRepService.selectSumQty(model, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Object> findBillImCheckRepByPage(BillCheckImRep rep,
			AuthorityParams authorityParams, boolean all) throws ManagerException {
		try {
			return this.billImCheckRepService.findBillImCheckRepByPage(rep, authorityParams, all);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}