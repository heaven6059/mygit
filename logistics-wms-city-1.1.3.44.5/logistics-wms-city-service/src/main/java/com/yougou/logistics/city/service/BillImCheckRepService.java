package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 入库查询service
 * @author chen.yl1
 *
 */
public interface BillImCheckRepService extends BaseCrudService {
	
	public List<SizeInfo> getSizeCodeByGroup(BillCheckImRep model, AuthorityParams authorityParams);
	
	public List<String> selectAllDtlSizeKind(BillCheckImRep model, AuthorityParams authorityParams);
	
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams);
	
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams);
	
	public List<BillCheckImRep> getBillImCheckDtlIm(BillCheckImRep model, AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtlUm(BillCheckImRep model, AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtlOtm(BillCheckImRep model, AuthorityParams authorityParams);
	public SumUtilMap<String, Object> selectSumQty(BillCheckImRep model, AuthorityParams authorityParams) throws ServiceException;
	
	public Map<String,Object> findBillImCheckRepByPage(BillCheckImRep rep, AuthorityParams authorityParams, boolean all)throws ServiceException;
}