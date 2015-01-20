package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.BillWmRecedeDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillWmRecedeDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillWmRecedeMapper;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-10-11 13:57:00
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Service("billWmRecedeDtlService")
class BillWmRecedeDtlServiceImpl extends BaseCrudServiceImpl implements
		BillWmRecedeDtlService {
	@Resource
	private BillWmRecedeDtlMapper billWmRecedeDtlMapper;

	@Resource
	private BillWmRecedeMapper billWmRecedeMapper;

	@Override
	public BaseCrudMapper init() {
		return billWmRecedeDtlMapper;
	}

	private static final String AREAQUALITYA = "A", AREAQUALITY6 = "6";
	private static final String RECEDETYPE02 = "02", RECEDETYPE01 = "01";

	@Override
	public short selectMaxPid(BillWmRecedeDtlKey billWmRecedeDtlKey)
			throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectMaxPid(billWmRecedeDtlKey);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectCountMx(BillWmRecedeDtlDto dto,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectCountMx(dto,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoByExpNo(
			BillWmRecedeDtlDto dto) throws ServiceException {
		try {
			return billWmRecedeDtlMapper.queryBillWmRecedeDtlDtoByExpNo(dto);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoGroupBy(
			SimplePage page, BillWmRecedeDtlDto dto,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billWmRecedeDtlMapper.queryBillWmRecedeDtlDtoGroupBy(page,
					dto,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectItemCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectItemCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecedeDtl> selectItem(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectItem(page, orderByField,
					orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectItemCountTest(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectItemCountTest(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecedeDtl> selectItemTest(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billWmRecedeDtlMapper.selectItemTest(page, orderByField,
					orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void excelImportData(List<BillWmRecedeDtl> list, String locno,
			String recedeNo, String ownerNo) throws ServiceException {
		try {
			// 查询主表信息
			BillWmRecede param = new BillWmRecede();
			param.setLocno(locno);
			param.setRecedeNo(recedeNo);
			param.setOwnerNo(ownerNo);
			BillWmRecede mainInfo = billWmRecedeMapper
					.selectByPrimaryKey(param);

			Map<String, Object> checkParam = new HashMap<String, Object>();
			checkParam.put("locno", mainInfo.getLocno());
			checkParam.put("supplierNo", mainInfo.getSupplierNo());
			checkParam.put("sysNo", mainInfo.getSysNo());
			if (RECEDETYPE01.equals(mainInfo.getRecedeType())) {
				checkParam.put("itemType", AREAQUALITY6);
			} else if (RECEDETYPE02.equals(mainInfo.getRecedeType())) {
				checkParam.put("areaQuality", AREAQUALITYA);
			}
			// 查询poId
			BillWmRecedeDtl dtlparam = new BillWmRecedeDtl();
			dtlparam.setLocno(locno);
			dtlparam.setRecedeNo(recedeNo);
			dtlparam.setOwnerNo(ownerNo);
			short poId = billWmRecedeDtlMapper.selectMaxPid(dtlparam);
			int index = 0;
			for (BillWmRecedeDtl dtl : list) {
				index++;
				checkParam.put("sizeNo", dtl.getSizeNo());
				checkParam.put("recedeQty", dtl.getRecedeQty());
				checkParam.put("itemNo", dtl.getItemNo());
				// 校验商品编码合法性
				int count = this.billWmRecedeDtlMapper.checkItem(checkParam);
				if (count == 0) {
					throw new ServiceException("第" + index + "条记录不符合规则");
				}
				dtl.setPoId(++poId);
				dtl.setPackQty(dtl.getPackQty() == null ? new BigDecimal(1)
						: dtl.getPackQty());
			}
			// 插入数据
			billWmRecedeDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findWmRecedeDtlDispatchCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecedeDtlMapper.selectWmRecedeDtlDispatchCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecedeDispatchDtlDTO> findWmRecedeDtlDispatchByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billWmRecedeDtlMapper.selectWmRecedeDtlDispatchByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecedeDtlMapper.selectDispatchSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}