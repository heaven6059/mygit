package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillImCheckDtlService;

/**
 * 
 * 收货验收单详情manager实现
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:15:15
 * @version 0.1.0
 * @copyright yougou.com
 */
@Service("billImCheckDtlManager")
class BillImCheckDtlManagerImpl extends BaseCrudManagerImpl implements BillImCheckDtlManager {
	@Resource
	private BillImCheckDtlService billImCheckDtlService;

	@Override
	public BaseCrudService init() {
		return billImCheckDtlService;
	}

	@Override
	public int selectCountMx(SizeComposeDTO dtoParam) {
		return billImCheckDtlService.selectCountMx(dtoParam);
	}

	@Override
	public List<SizeComposeDTO> queryBillImCheckDTOGroupBy(SimplePage page, SizeComposeDTO dtoParam) {
		return billImCheckDtlService.queryBillImCheckDTOGroupBy(page, dtoParam);
	}

	@Override
	public List<SizeComposeDTO> queryBillImCheckDTOBExpNo(SizeComposeDTO dtoParam) {
		return billImCheckDtlService.queryBillImCheckDTOBExpNo(dtoParam);
	}

	/**
	 * 查询所有sizecode
	 */
	@Override
	public List<SizeInfo> getSizeCodeByGroup() {
		return billImCheckDtlService.getSizeCodeByGroup();
	}

	/**
	 * 查询入库订单报表明细信息
	 */
	@Override
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlService.getBillImCheckDtl(model, authorityParams);
	}

	/**
	 * 查询总数
	 */
	@Override
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlService.getCount(model, authorityParams);
	}

	/**
	 * 分组计算入库订单编号和商品编码
	 */
	@Override
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlService.getBillImCheckByGroup(model, authorityParams);
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImCheckDtlService.selectSumQty(map, authorityParams);
	}
	
	public SumUtilMap<String, Object> selectDtlSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImCheckDtlService.selectDtlSumQty(map, authorityParams);
	}

	@Override
	public List<String> selectAllDtlSizeKind(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlService.selectAllDtlSizeKind(model, authorityParams);
	}

	@Override
	public int selectCheckDtlDiffCount(BillImCheckDtl billImCheckDtl) throws ManagerException {
		try{
			return billImCheckDtlService.selectCheckDtlDiffCount(billImCheckDtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int selectCheckDtlDiffQty(BillImCheckDtl billImCheckDtl) throws ManagerException {
		try{
			return billImCheckDtlService.selectCheckDtlDiffQty(billImCheckDtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillImCheckDtl> findBillImCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billImCheckDtlService.findBillImCheckDtlByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findBillImCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billImCheckDtlService.findBillImCheckDtlCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}