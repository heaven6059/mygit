package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BillImImportService;
import com.yougou.logistics.city.service.BillImReceiptDtlService;
import com.yougou.logistics.city.service.BillImReceiptService;
import com.yougou.logistics.city.service.BmContainerService;

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
@Service("billImReceiptManager")
class BillImReceiptManagerImpl extends BaseCrudManagerImpl implements BillImReceiptManager {
	@Resource
	private BillImReceiptService billImReceiptService;
	@Resource
	private BillImReceiptDtlService billImReceiptDtlService;
	
	@Resource
	private BmContainerService bmContainerService;
	
//	private static final String STATUS11 = "11";

	private static final String STATUS10 = "10";

	@Override
	public BaseCrudService init() {
		return billImReceiptService;
	}

	@Override
	public List<BillImReceipt> findReceiptByPage(SimplePage page, BillImReceipt receipt, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billImReceiptService.findReceiptByPage(page, receipt, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int selectCountMx(BillImReceipt receipt, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billImReceiptService.selectCountMx(receipt, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	public void saveMain(BillImReceipt receipt) throws ManagerException {
		try {
			receipt.setStatus(STATUS10);
			billImReceiptService.saveMain(receipt);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void save(BillImReceipt receipt, List<BillImImportDtlDto> insertDetail) throws ManagerException {
		try {
			billImReceiptService.save(receipt, insertDetail);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> update(BillImReceipt receipt, List<BillImImportDtlDto> insert,
			List<BillImImportDtlDto> update, List<BillImImportDtlDto> del,SystemUser user) throws ManagerException {
		try {
			return this.billImReceiptService.update(receipt, insert, update, del,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int auditBatch(String keyStr, String locno, String ownerNo,SystemUser user) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BillImReceipt receipt = null;
			for (String obj : strs) {
				try {
					receipt = new BillImReceipt();
					receipt.setAuditor(user.getLoginName());
					receipt.setAuditorName(user.getUsername());
					receipt.setAudittm(new Date());
					receipt.setLocno(locno);
					receipt.setOwnerNo(ownerNo);
					receipt.setReceiptNo(obj);
					receipt.setStatus(STATUS10);
					count += billImReceiptService.auditReceipt(receipt);
				} catch (Exception e) {
					throw new ManagerException(e.getMessage());
				}
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBatch(String keyStr, String locno, String ownerNo, SystemUser user) throws ManagerException {
		int count = 0;
		Date date = new Date();
		try {
			if (StringUtils.isNotBlank(keyStr)) {
				String[] strs = keyStr.split(",");
				BillImReceipt receipt = null;
				Map<String,BmContainer> bmMap=new HashMap<String,BmContainer>();
				for (String obj : strs) {
					    //删除主单
						receipt = new BillImReceipt();
						receipt.setLocno(locno);
						receipt.setOwnerNo(ownerNo);
						receipt.setReceiptNo(obj);
						receipt.setEditor(user.getLoginName());
						receipt.setEdittm(date);
						receipt.setCheckStatus(STATUS10);
						int a = billImReceiptService.deleteById(receipt);
						if(a < 1){
							throw new ManagerException("单据"+obj+"已删除或状态已改变，不能进行删除操作！");
						}
						//删除从表
						Map<String, String> map = new HashMap<String, String>();
						map.put("receiptNo", obj);
						map.put("locno", locno);
						List<BillImImportDtlDto> dtlList = billImReceiptDtlService.findDetailAll(map);
						for (BillImImportDtlDto dto : dtlList) {
							billImReceiptService.delDtal(receipt, dto);
							String panNo=dto.getPanNo();
							if(StringUtils.isNotEmpty(panNo)){
								BmContainer panBm=new BmContainer();
								panBm.setFalg("true");
								panBm.setLocno(locno);
								panBm.setConNo(panNo);
								panBm.setStatus("0");
								bmMap.put(panNo, panBm);//解除板锁定状态
							}
							String boxNo=dto.getBoxNo();
							BmContainer boxBm=new BmContainer();
							boxBm.setFalg("true");
							boxBm.setLocno(locno);
							boxBm.setConNo(boxNo);
							boxBm.setStatus("0");
							bmMap.put(boxNo, boxBm);//解除箱锁定状态
						}
						//解除容器锁定状态
						count++;
				}
				//批量处理容器解锁
				if(bmMap.size()>0){
					List<BmContainer> bmList=new ArrayList<BmContainer>();
					Iterator<?> iter = bmMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						bmList.add((BmContainer) entry.getValue());
					}
					bmContainerService.batchUpdate(bmList);
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
		return count;
	}

	@Override
	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findMainReciptCount(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findMainRecipt(page, map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceipt> findMainReciptSum(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findMainReciptSum(page, map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillImReceipt> findReciptNoChecked(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findReciptNoChecked(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findReciptNoCheckedCount(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findReciptNoCheckedCount(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectCount4Direct(Map map) throws ManagerException {
		try {
			return billImReceiptService.selectCount4Direct(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImImport> selectByPage4Direct(SimplePage page, Map map) throws ManagerException {
		try {
			return billImReceiptService.selectByPage4Direct(page, map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillImImportDtlDto> findBatchSelectBox(List<String> importNoList,Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.selectBatchSelectBox(importNoList,map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceipt> findImReceiptPrint(Map<?, ?> map,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImReceiptService.findImReceiptPrint(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

}