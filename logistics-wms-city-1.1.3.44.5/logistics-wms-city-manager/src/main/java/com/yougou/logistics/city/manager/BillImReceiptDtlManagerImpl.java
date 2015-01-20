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
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillImReceiptDtlService;

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
@Service("billImReceiptDtlManager")
class BillImReceiptDtlManagerImpl extends BaseCrudManagerImpl implements BillImReceiptDtlManager {
	@Resource
	private BillImReceiptDtlService billImReceiptDtlService;

	@Override
	public BaseCrudService init() {
		return billImReceiptDtlService;
	}

	@Override
	public int findDetailCount(Map<?, ?> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptDtlService.findDetailCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImImportDtlDto> findDetail(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.findDetail(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findAllDetailByReciptNo(BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.findAllDetailByReciptNo(dtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findItemNotInReceiptCount(Item item, BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.findItemNotInReceiptCount(item, dtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findItemNotInReceipt(Item item, BillImReceiptDtl dtl, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptDtlService.findItemNotInReceipt(item, dtl, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findDtlByItemNoAndSizeNo(BillImReceiptDtl dtl) throws ManagerException {
		try {
			return billImReceiptDtlService.findDtlByItemNoAndSizeNo(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> selectItemDetail(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptDtlService.selectItemDetail(map, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItemDetailCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptDtlService.selectItemDetailCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findBillImReceiptDtlBox(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.findBillImReceiptDtlBox(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return this.billImReceiptDtlService.selectSumQty(map, authorityParams);
	}

	@Override
	public String selectSysNo(Map<String, String> map) throws ManagerException {
		try {
			return billImReceiptDtlService.selectSysNo(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.selectItemSizeKind(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImReceiptDtlService.selectItemDetailByGroupCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ManagerException {
		try {
			return billImReceiptDtlService.selectItemDetailByGroup(map, authorityParams, page);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> selectDetailBySizeNo(Map<String, Object> map) throws ManagerException {
		try {
			return billImReceiptDtlService.selectDetailBySizeNo(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptDtlService.selectBoxQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<String> findSysNoList(Map<String, Object> map)
			throws ManagerException {
		try {
			return billImReceiptDtlService.findSysNoList(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> findDataBySys(String locno, String receiptNo,
			String sysNo,List<BillImReceiptDtl> dtls) throws ManagerException {
		try {
			return billImReceiptDtlService.findDataBySys(locno, receiptNo, sysNo,dtls);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceiptDtl> find4Export(Map<String, Object> map)
			throws ManagerException {
		try {
			return billImReceiptDtlService.find4Export(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}