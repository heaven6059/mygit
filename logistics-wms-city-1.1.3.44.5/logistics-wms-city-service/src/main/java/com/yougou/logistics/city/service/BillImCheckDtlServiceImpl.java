package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImCheckDtlKey;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillImCheckDtlMapper;

/**
 * 
 * 收货验收单详情service实现
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:16:32
 * @version 0.1.0
 * @copyright yougou.com
 */
@Service("billImCheckDtlService")
class BillImCheckDtlServiceImpl extends BaseCrudServiceImpl implements BillImCheckDtlService {
	@Resource
	private BillImCheckDtlMapper billImCheckDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billImCheckDtlMapper;
	}

	@Override
	public Integer selectMaxRowId(BillImCheckDtlKey billImCheckDtlKey) throws ServiceException {
		return billImCheckDtlMapper.selectMaxRowId(billImCheckDtlKey);
	}

	@Override
	public int selectCountMx(SizeComposeDTO dtoParam) {
		return billImCheckDtlMapper.selectCountMx(dtoParam);
	}

	@Override
	public List<SizeComposeDTO> queryBillImCheckDTOGroupBy(SimplePage page, SizeComposeDTO dtoParam) {
		return billImCheckDtlMapper.queryBillImCheckDTOGroupBy(page, dtoParam);
	}

	@Override
	public List<SizeComposeDTO> queryBillImCheckDTOBExpNo(SizeComposeDTO dtoParam) {
		return billImCheckDtlMapper.queryBillImCheckDTOBExpNo(dtoParam);
	}

	@Override
	public List<SizeInfo> getSizeCodeByGroup() {
		List<SizeInfo> list = new ArrayList<SizeInfo>();
		list = billImCheckDtlMapper.getSizeCodeByGroup();
		return list;
	}

	/**
	 * 查询入库订单明细信息
	 */
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams) {

		return billImCheckDtlMapper.getBillImCheckDtl(model, authorityParams);
	}

	/**
	 * 查询记录总数
	 */
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlMapper.getCount(model, authorityParams);
	}

	/**
	 * 通过商品编码和入库订单编号分组查询
	 */
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlMapper.getBillImCheckByGroup(model, authorityParams);
	}

	/**
	 * 验证商品重复性
	 */
	@Override
	public List<BillImCheckDtl> findCheckDtlGroupByCount(BillImCheckDtl billImCheckDtl) {
		return billImCheckDtlMapper.selectCheckDtlGroupByCount(billImCheckDtl);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImCheckDtlMapper.selectSumQty(map, authorityParams);
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckDtlMapper.selectAllDtlSizeKind(model, authorityParams);
	}

	@Override
	public int selectCheckDtlDiffCount(BillImCheckDtl billImCheckDtl) throws ServiceException {
		try{
			return billImCheckDtlMapper.selectCheckDtlDiffCount(billImCheckDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectCheckDtlDiffQty(BillImCheckDtl billImCheckDtl) throws ServiceException {
		try{
			return billImCheckDtlMapper.selectCheckDtlDiffQty(billImCheckDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImCheckDtl> findBillImCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billImCheckDtlMapper.selectBillImCheckDtlByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findBillImCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billImCheckDtlMapper.selectBillImCheckDtlCount(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectDtlSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImCheckDtlMapper.selectDtlSumQty(map, authorityParams);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImCheckDtl> selectCheckDetailByReciptNo(
			Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billImCheckDtlMapper.selectCheckDetailByReciptNo(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void insertBathCheckDtl(List<BillImCheckDtl> lstCheckDtl)
			throws ServiceException {
		try{
			 billImCheckDtlMapper.insertBathCheckDtl(lstCheckDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImCheckDtl> selectGroupByBox(Map<String, Object> params) throws ServiceException {
		try{
			return billImCheckDtlMapper.selectGroupByBox(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectCheckDtlPanNum(BillImCheckDtl billImCheckDtl) throws ServiceException {
		try{
			return billImCheckDtlMapper.selectCheckDtlPanNum(billImCheckDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}