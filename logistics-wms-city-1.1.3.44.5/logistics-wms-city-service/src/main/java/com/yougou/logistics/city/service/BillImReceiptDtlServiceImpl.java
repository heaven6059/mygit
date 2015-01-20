package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.SizeInfoMapper;
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
@Service("billImReceiptDtlService")
class BillImReceiptDtlServiceImpl extends BaseCrudServiceImpl implements BillImReceiptDtlService {
	@Resource
	private BillImReceiptDtlMapper billImReceiptDtlMapper;

	@Resource
	private SizeInfoMapper sizeInfoMapper;
	@Override
	public BaseCrudMapper init() {
		return billImReceiptDtlMapper;
	}

	@Override
	public int deleteByPrimarayKeyForReceiptNo(Object obj) throws ServiceException {
		try {
			return billImReceiptDtlMapper.deleteByPrimarayKeyForReceiptNo(obj);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findDetailCount(Map<?, ?> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDetailCount(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImImportDtlDto> findDetail(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDetail(page, map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<String> selectBoxNoByImportNo(String importNo) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectBoxNoByImportNo(importNo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImImportDtlDto> findDetailAll(Map<?, ?> map) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDetailAll(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateStatusByImportNo(BillImReceiptDtl dtl) throws ServiceException {
		try {
			return billImReceiptDtlMapper.updateStatusByImportNo(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImReceiptDtl> findAllDetailByReciptNo(BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectAllDetailByReciptNo(dtl, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findItemNotInReceiptCount(Item item, BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemNotInReceiptCount(item, dtl, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImReceiptDtl> findItemNotInReceipt(Item item, BillImReceiptDtl dtl, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemNotInReceipt(item, dtl, page, authorityParams);
		} catch (DaoException e) {
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
	public List<BillImReceiptDtl> selectItemDetail(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetail(map, page, authorityParams);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectItemDetailCount(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetailCount(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public BillImReceiptDtl selectDtlByItemNo(BillImReceiptDtl dtl) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDtlByItemNo(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImReceiptDtl> findBillImReceiptDtlBox(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectBillImReceiptDtlBox(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return this.billImReceiptDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	public String selectSysNo(Map<String, String> map) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectSysNo(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemSizeKind(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetailByGroupCount(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectItemDetailByGroup(map, authorityParams, page);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> selectDetailBySizeNo(Map<String, Object> map) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectDetailBySizeNo(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectBoxQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public BillImReceiptDtl selectReceiptDtlPanByBox(BillImReceiptDtl billImReceiptDtl) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectReceiptDtlPanByBox(billImReceiptDtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer selectMaxRowId(BillImReceipt billImReceipt) throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectMaxRowId(billImReceipt);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<String> findSysNoList(Map<String, Object> map)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.selectSysNoList(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findDataBySys(String locno, String receiptNo,
			String sysNo,List<BillImReceiptDtl> dtls) throws ServiceException {
		try {
			List<BillImReceiptDtl> rs = getDataBySys(sysNo, dtls);
			if(rs != null && rs.size() > 0){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("sysNo", sysNo);
				List<SizeInfo> sizeTypeList = sizeInfoMapper.selectByParams(null, params);
				if(sizeTypeList == null || sizeTypeList.size() == 0){
					return null;
				}
				Map<String, Map<String, SizeInfo>> sysSizeMap = new HashMap<String, Map<String,SizeInfo>>();
				Map<String, SizeInfo> skSizeMap = null;
				String sizeKind = null;
				for(SizeInfo si :sizeTypeList){
					sizeKind = si.getSizeKind();
					if((skSizeMap = sysSizeMap.get(sizeKind)) != null){
						skSizeMap.put(si.getSizeNo(), si);
					}else{
						skSizeMap = new HashMap<String, SizeInfo>();
						skSizeMap.put(si.getSizeNo(), si);
						sysSizeMap.put(sizeKind, skSizeMap);
					}
				}
				String key = null;
				Map<String, BillImReceiptDtl> rows = new HashMap<String, BillImReceiptDtl>();
				BillImReceiptDtl frist = null;
				for(BillImReceiptDtl dtl:rs){
					key = dtl.getImportNo() + "_" + dtl.getItemNo();
					int hcolNo = sysSizeMap.get(dtl.getSizeKind()).get(dtl.getSizeNo()).getHcolNo().intValue();
					String filedName="setV"+hcolNo;
					Object[] arg=new Object[]{String.valueOf(dtl.getReceiptQty())};
					if((frist = rows.get(key)) != null){
						frist.setAllCount(frist.getAllCount().add(dtl.getReceiptQty()));
						CommonUtil.invokeMethod(frist,filedName,arg);
					}else{
						CommonUtil.invokeMethod(dtl,filedName,arg);
						dtl.setAllCount(dtl.getReceiptQty());
						rows.put(key, dtl);
					}
				}
				if(rows.size() > 0){
					List<BillImReceiptDtl> data = new ArrayList<BillImReceiptDtl>();
					for(Entry<String, BillImReceiptDtl> m:rows.entrySet()){
						data.add(m.getValue());
					}
					return data;
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return new ArrayList<BillImReceiptDtl>();
	}
	private List<BillImReceiptDtl> getDataBySys(String sysNo,List<BillImReceiptDtl> dtls){
		if(dtls == null || dtls.size() == 0){
			return null;
		}
		List<BillImReceiptDtl> rs = new ArrayList<BillImReceiptDtl>();
		Iterator<BillImReceiptDtl> it = dtls.iterator();
		BillImReceiptDtl dtl = null;
		while(it.hasNext()){
			dtl = it.next();
			if(dtl.getItemNo().startsWith(sysNo)){
				rs.add(dtl);
				it.remove();
			}
		}
		return rs;
	}

	
	@Override
	public List<BillImReceiptDtl> find4Export(Map<String, Object> map)
			throws ServiceException {
		try {
			return billImReceiptDtlMapper.select4Export(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}