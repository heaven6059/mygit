package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmUntreadDtlStatusEnums;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
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
@Service("billUmUntreadDtlService")
class BillUmUntreadDtlServiceImpl extends BaseCrudServiceImpl implements BillUmUntreadDtlService {
	@Resource
	private BillUmUntreadDtlMapper billUmUntreadDtlMapper;
	@Resource
	private BillUmUntreadMapper billUmUntreadMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmUntreadDtlMapper;
	}

	private static final String ADDFLAG0 = "0";

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveUntreadDtl(List<ConBoxDtl> insertList, List<ConBoxDtl> updateList, List<ConBoxDtl> deleteList,
			BillUmUntread untread) throws ServiceException {
		try {
			// 删除行
			for (ConBoxDtl dtl : deleteList) {
				BillUmUntreadDtl untreadDtl = new BillUmUntreadDtl();
				untreadDtl.setLocno(untread.getLocno());
				untreadDtl.setOwnerNo(untread.getOwnerNo());
				untreadDtl.setBoxNo(dtl.getBoxNo());
				billUmUntreadDtlMapper.deleteItemByBox(untreadDtl);
			}
			//记录修改新信息
			BillUmUntread untreadMainUpdate = new BillUmUntread();
			untreadMainUpdate.setEditor(untread.getEditor());
			untreadMainUpdate.setEditorName(untread.getEditorName());
			untreadMainUpdate.setLocno(untread.getLocno());
			untreadMainUpdate.setOwnerNo(untread.getOwnerNo());
			untreadMainUpdate.setUntreadNo(untread.getUntreadNo());
			billUmUntreadMapper.updateByPrimaryKeySelective(untreadMainUpdate);
			
			for (ConBoxDtl dtl : insertList) {
				BillUmUntreadDtl untreadDtl = new BillUmUntreadDtl();
				untreadDtl.setLocno(untread.getLocno());
				untreadDtl.setOwnerNo(untread.getOwnerNo());
				untreadDtl.setBoxNo(dtl.getBoxNo());
				List<BillUmUntreadDtl> list = billUmUntreadDtlMapper.selectItemInBox(untreadDtl);
				for (BillUmUntreadDtl result : list) {
					Integer rowId = billUmUntreadDtlMapper.selectMaxRowId(untread);
					result.setRowId(rowId + 1);
					result.setLocno(untread.getLocno());
					result.setOwnerNo(untread.getOwnerNo());
					result.setUntreadNo(untread.getUntreadNo());
					result.setBoxNo(dtl.getBoxNo());
					result.setAddFlag(ADDFLAG0);
					result.setReceiptQty(new BigDecimal(0));
					result.setStatus(BillUmUntreadDtlStatusEnums.STATUS10.getStatus());
					result.setCheckQty(new BigDecimal(0));
					billUmUntreadDtlMapper.insertSelective(result);
				}
			}

			Set<String> repeatList = this.billUmUntreadDtlMapper.selectRepeat(untread);
			if (repeatList != null && repeatList.size() > 0) {
				StringBuilder str = new StringBuilder();
				for (String dtl : repeatList) {
					str.append("箱号:").append(dtl).append("<br>");
				}
				str.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ServiceException(str.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillUmUntreadDtl> selectAllBox(BillUmUntread untread, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmUntreadDtlMapper.selectAllBox(untread, authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	@Override
	public int findCountByBox(Map<String, Object> params) throws ServiceException {
		try {
			return billUmUntreadDtlMapper.selectCountByBox(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public List<BillUmUntreadDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ServiceException {
		try {
			return this.billUmUntreadDtlMapper.selectByPageByBox(page, params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public int modifyByItemAndKey(BillUmUntreadDtl billUmUntreadDtl) throws ServiceException {
		try {
			return billUmUntreadDtlMapper.updateByItemAndKey(billUmUntreadDtl);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<ConBoxDtl> select4Box(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmUntreadDtlMapper.select4Box(page, params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int select4BoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmUntreadDtlMapper.select4BoxCount(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Map<String, Object>> queryPrints(String locno, String keystr) throws ServiceException {
		try {
			if (keystr == null) {
				throw new ServiceException("请求参数错误");
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			String[] keys = keystr.split(",");
			for (String key : keys) {
				List<Map<String, Object>> dtlList = new ArrayList<Map<String, Object>>();
				Map<String, Object> single = new HashMap<String, Object>();
				Set<String> allSizeNo = new HashSet<String>();
				single.put("list", dtlList);
				single.put("allSizeNo", allSizeNo);
				resultList.add(single);
				String[] subKey = key.split("\\|");
				BillUmUntread untread = new BillUmUntread();
				untread.setLocno(locno);
				untread.setUntreadNo(subKey[0]);
				untread.setOwnerNo(subKey[1]);
				BillUmUntread resultUntread = this.billUmUntreadMapper.selectDetail4Print(untread);
				single.put("untread", resultUntread);
				BigDecimal allSum = new BigDecimal(0);
				List<BillUmUntreadDtl> untreadList = billUmUntreadDtlMapper.selectAllDetail(untread);
				Map<String, Object> obj = new HashMap<String, Object>();
				for (BillUmUntreadDtl dtl : untreadList) {
					String itemInfo = String.valueOf(obj.get(dtl.getBoxNo() + dtl.getItemNo()));
					allSizeNo.add(dtl.getSizeNo());
					if (itemInfo == null || "null".equals(itemInfo)) {
						// 根据箱号，商品编码确定一行记录
						obj = new HashMap<String, Object>();
						obj.put(dtl.getBoxNo() + dtl.getItemNo(), dtl.getBoxNo() + dtl.getItemNo());
						obj.put(dtl.getSizeNo(), dtl.getItemQty());
						obj.put("untreadDtl", dtl);
						obj.put(dtl.getSizeNo(), dtl.getItemQty());
						obj.put("sumQty", dtl.getItemQty());
						allSum = allSum.add(dtl.getItemQty());
						dtlList.add(obj);
					} else {
						obj.put(dtl.getSizeNo(), dtl.getItemQty());
						BigDecimal sumQty = (BigDecimal) obj.get("sumQty");
						obj.put("sumQty", sumQty.add(dtl.getItemQty()));
						allSum = allSum.add(dtl.getItemQty());
					}
				}
				single.put("allSum", allSum);
			}
			return resultList;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmUntreadDtlMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}