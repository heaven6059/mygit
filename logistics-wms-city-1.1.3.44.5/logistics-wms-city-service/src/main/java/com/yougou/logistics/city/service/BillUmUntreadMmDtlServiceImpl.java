package com.yougou.logistics.city.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmUntreadMmDtlStatusEnums;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMmDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMmMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
@Service("billUmUntreadMmDtlService")
class BillUmUntreadMmDtlServiceImpl extends BaseCrudServiceImpl implements BillUmUntreadMmDtlService {
	@Resource
	private BillUmUntreadMmDtlMapper billUmUntreadMmDtlMapper;
	
	@Resource
	private BillUmUntreadMmMapper billUmUntreadMmMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmUntreadMmDtlMapper;
	}

	@Override
	public List<BillUmUntreadMmDtl> selectItem(SimplePage page, BillUmUntreadMmDtl dtl) throws ServiceException {
		try {
			return billUmUntreadMmDtlMapper.selectItem(page, dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectItemCount(BillUmUntreadMmDtl dtl) throws ServiceException {
		try {
			return billUmUntreadMmDtlMapper.selectItemCount(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveUntreadMmDtl(List<BillUmUntreadMmDtl> insertList, List<BillUmUntreadMmDtl> updateList,
			List<BillUmUntreadMmDtl> deleteList, BillUmUntreadMm untread) throws ServiceException {
		try {
			//删除行
			for (BillUmUntreadMmDtl dtl : deleteList) {
				billUmUntreadMmDtlMapper.deleteByPrimarayKeyForModel(dtl);
			}
			//更新的行
			for (BillUmUntreadMmDtl dtl : updateList) {
				billUmUntreadMmDtlMapper.updateByPrimaryKeySelective(dtl);
			}
			//新增的行
			Date curDate = new Date();
			//查询通知单下的所有明细
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("untreadMmNo", untread.getUntreadMmNo());
			params.put("ownerNo", untread.getOwnerNo());
			params.put("locno", untread.getLocno());
			List<BillUmUntreadMmDtl> allList = billUmUntreadMmDtlMapper.selectByParams(null, params);
			for (BillUmUntreadMmDtl dtl : insertList) {
				for (BillUmUntreadMmDtl all : allList) {
					if (all.getItemNo().equals(dtl.getItemNo()) && all.getSizeNo().equals(dtl.getSizeNo())) {
						throw new DaoException("商品编码：" + dtl.getItemName() + "<br>尺码：" + dtl.getSizeNo() + ",已经存在!");
					}
				}
				Integer rowId = billUmUntreadMmDtlMapper.selectMaxRowId(untread);
				dtl.setLocno(untread.getLocno());
				dtl.setRowId(rowId + 1);
				dtl.setOwnerNo(untread.getOwnerNo());
				dtl.setCreator(untread.getCreator());
				dtl.setCreatetm(curDate);
				dtl.setUntreadMmNo(untread.getUntreadMmNo());
				dtl.setStatus(BillUmUntreadMmDtlStatusEnums.STATUS10.getStatus());
				billUmUntreadMmDtlMapper.insertSelective(dtl);
			}
			untread.setEditor(untread.getCreator());
			untread.setEdittm(new Date());
			billUmUntreadMmMapper.updateByPrimaryKeySelective(untread);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}

	}

	public List<BillUmUntreadMmDtl> selectStoreNo(BillUmUntreadMm mm) throws ServiceException {
		try {
			return billUmUntreadMmDtlMapper.selectStoreNo(mm);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params) throws ServiceException {
		try {
			return billUmUntreadMmDtlMapper.selectSumQty(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}