package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.BillImImportDtlKey;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BillImImportDtlService;
import com.yougou.logistics.city.service.BillImImportService;
import com.yougou.logistics.city.service.BillImReceiptService;
import com.yougou.logistics.city.service.ConBoxDtlService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ProcCommonService;
import com.yougou.logistics.city.service.StoreService;
import com.yougou.logistics.city.service.SupplierService;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
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
@Service("billImImportManager")
class BillImImportManagerImpl extends BaseCrudManagerImpl implements
	BillImImportManager {
    @Resource
    private BillImImportService billImImportService;

    @Resource
    private ProcCommonService procCommonService;

    @Resource
    private ConBoxService conBoxService;

    @Resource
    private BillImImportDtlService billImImportDtlService;

    @Resource
    private BillImReceiptService billImReceiptService;  

    @Resource
    private ConBoxDtlService conBoxDtlService;

    @Resource
	private SupplierService supplierService;
    
    @Resource
	private StoreService storeService;
    
    @Override
    public BaseCrudService init() {
	return billImImportService;
    }

    private static final String STATSU10 = "10", STATUS11 = "11",
	    STATSU91 = "91", STATSU30 = "30",STATSU20 = "20";

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public boolean deleteImImport(String noStrs,AuthorityParams authorityParams) throws ManagerException {
	try {
	    if (StringUtils.isNotBlank(noStrs)) {
		String[] strs = noStrs.split(",");
		for (String obj : strs) {
		    String[] imImportArray = obj.split("#");
		    if (imImportArray.length > 2) {
			String importNo = imImportArray[0];
			String locno = imImportArray[1];
			String ownerNo = imImportArray[2];
			
			//用户权限校验
//			boolean authority=billAuthorityService.isHasFullBillAuthority(TableContants.BILL_IM_IMPORT_DTL,importNo,authorityParams);
//			if(!authority){
//				throw new ManagerException("【"+importNo+"】用户品牌权限不足，删除失败！");
//			}
			
			// 查询主表下面的所有箱号
			BillImImportDtlKey billImImportDtlKey = new BillImImportDtlKey();
			billImImportDtlKey.setImportNo(importNo);
			billImImportDtlKey.setLocno(locno);
			billImImportDtlKey.setOwnerNo(ownerNo);
			List<BillImImportDtl> lstBillImImport = billImImportDtlService
				.selectBoxNoListByDetail(billImImportDtlKey);
			for (BillImImportDtl detailVo : lstBillImImport) {
				
				if(StringUtils.isEmpty(detailVo.getBoxNo())){
					throw new ManagerException("通知单明细中的箱号为空，删除失败！");
				}
				
				
			    // 根据箱号把箱信息的入库单号清空
			    ConBox conBox = new ConBox();
			    conBox.setBoxNo(detailVo.getBoxNo());
			    conBox.setLocno(locno);
			    conBox.setOwnerNo(ownerNo);
			    conBox.setsImportNo("9999");// 9999表示清空
			    int b = conBoxService.modifyById(conBox);
			    if (b < 1) {
				throw new ManagerException("清空箱号【"
					+ detailVo.getBoxNo()
					+ "】的入库单号信息时未更新到记录！");
			    }
			    
			    ConBoxDtl cbDtl = new ConBoxDtl();
				cbDtl.setImportNo("");
				cbDtl.setLocno(locno);
				cbDtl.setOwnerNo(ownerNo);
				cbDtl.setBoxNo(detailVo.getBoxNo());
				conBoxDtlService.modifyById(cbDtl);
			}

			// 删除主表
			BillImImport billImImport = new BillImImport();
			billImImport.setImportNo(importNo);
			billImImport.setLocno(locno);
			billImImport.setOwnerNo(ownerNo);
			billImImport.setStatus(STATSU10);
			int b = billImImportService.deleteByPrimarayKeyForModel(billImImport);
			if (b < 1) {
			    throw new ManagerException("单据" + importNo + "已删除或状态已改变，不能进行删除操作！");
			} else {
			    // 删除明细表
			    billImImportDtlService
				    .deleteImImportDtl(billImImportDtlKey);
			}
		    }
		}
	    }
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	return true;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
//    public Map<String, Object> auditImImport(String noStrs, String locno,
//	    String loginName,AuthorityParams authorityParams) throws ManagerException {
//	Map<String, Object> objMap = new HashMap<String, Object>();
//	StringBuilder strBuilder = new StringBuilder();
//	String flag = "false";
//	String resultMsg = "";
//	Date date = new Date();
//	try {
//	    if (StringUtils.isNotBlank(noStrs)) {
//		String[] strs = noStrs.split(",");
//		
//		for (String obj : strs) {
//		    String[] imImportArray = obj.split("#");
//		    if (imImportArray.length > 1) {
//			String importNo = imImportArray[0];
//			String ownerNo = imImportArray[1];
//			
//			//用户权限校验
////			boolean authority=billAuthorityService.isHasFullBillAuthority(TableContants.BILL_IM_IMPORT_DTL,importNo, authorityParams);
////			if(!authority){
////				throw new ManagerException("【"+importNo+"】用户品牌权限不足，审核失败！");
////			}
//			
//
//			BillImImportDtlKey billImImportDtlKey = new BillImImportDtlKey();
//			billImImportDtlKey.setImportNo(importNo);
//			billImImportDtlKey.setLocno(locno);
//			billImImportDtlKey.setOwnerNo(ownerNo);
//			int a = billImImportDtlService
//				.selectMaxPid(billImImportDtlKey);
//			if (a < 1) {
//			    strBuilder.append(importNo + "，");
//			}
//		    }
//		}
//		// 如果有值
//		if (strBuilder.length() > 0) {
//		    strBuilder.insert(0, "单号：");
//		    strBuilder.append("没有添加明细，无法审核！");
//		    flag = "warn";
//		    resultMsg = strBuilder.toString();
//		} else {
//		    for (String obj : strs) {
//			String[] imImportArray = obj.split("#");
//			String importNo = imImportArray[0];
//			String ownerNo = imImportArray[1];
//			BillImImport billImImport = new BillImImport();
//			billImImport.setImportNo(importNo);
//			billImImport.setLocno(locno);
//			billImImport.setOwnerNo(ownerNo);
//			billImImport.setStatus(STATUS11);// 状态10-建单；11-审核；12-验收中；13-结案；14-预约；15-打印预验收；16-取消；17-验收取消中
//			billImImport.setAudittm(date);
//			billImImport.setAuditor(loginName);
//			billImImport.setEdittm(date);
//			billImImport.setEditor(loginName);
//			int a = billImImportService.modifyById(billImImport);
//			if (a < 1) {
//			    throw new ManagerException("审核单号【" + importNo
//				    + "】时未更新到记录！");
//			}
//		    }
//		    flag = "true";
//		}
//	    }
//	} catch (Exception e) {
//	    throw new ManagerException(e);
//	}
//	objMap.put("flag", flag);
//	objMap.put("resultMsg", resultMsg);
//	return objMap;
//    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public Map<String, Object> auditImImport(String noStrs, SystemUser user,AuthorityParams authorityParams) throws ManagerException {
	Map<String, Object> objMap = new HashMap<String, Object>();
	StringBuilder strBuilder = new StringBuilder();
	String flag = "false";
	String resultMsg = "";
	Date date = new Date();
	try {
	    if (StringUtils.isNotBlank(noStrs)) {
			String[] strs = noStrs.split(",");
			
			for (String obj : strs) {
			    String[] imImportArray = obj.split("#");
			    if (imImportArray.length > 1) {
					String importNo = imImportArray[0];
					String ownerNo = imImportArray[1];
					
					//用户权限校验
		//			boolean authority=billAuthorityService.isHasFullBillAuthority(TableContants.BILL_IM_IMPORT_DTL,importNo, authorityParams);
		//			if(!authority){
		//				throw new ManagerException("【"+importNo+"】用户品牌权限不足，审核失败！");
		//			}
					
					BillImImport billImImport = new BillImImport();
					billImImport.setImportNo(importNo);
					billImImport.setLocno(user.getLocNo());
					billImImport.setOwnerNo(ownerNo);
					billImImport.setStatus(STATUS11);// 状态10-建单；11-审核；12-验收中；13-结案；14-预约；15-打印预验收；16-取消；17-验收取消中
					billImImport.setAudittm(date);
					billImImport.setAuditor(user.getLoginName());
					billImImport.setEdittm(date);
					billImImport.setEditor(user.getLoginName());
					billImImport.setAuditorName(user.getUsername());
					billImImport.setEditorName(user.getUsername());
					billImImport.setUptStatus(STATSU10);
					int x = billImImportService.modifyById(billImImport);
					if (x < 1) {
					    throw new ManagerException("单据" + importNo + "已删除或状态已改变，不能进行审核操作！");
					}
		
					BillImImportDtlKey billImImportDtlKey = new BillImImportDtlKey();
					billImImportDtlKey.setImportNo(importNo);
					billImImportDtlKey.setLocno(user.getLocNo());
					billImImportDtlKey.setOwnerNo(ownerNo);
					int a = billImImportDtlService
						.selectMaxPid(billImImportDtlKey);
					if (a < 1) {
					    strBuilder.append(importNo + "，");
					}
			    }
			}
			// 如果有值
			if (strBuilder.length() > 0) {
			    strBuilder.insert(0, "单号：");
			    strBuilder.append("没有添加明细，无法审核！");
			    resultMsg = strBuilder.toString();
			    throw new ManagerException(resultMsg);
			}else{
				flag = "true";
			}
	    }
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	objMap.put("flag", flag);
	objMap.put("resultMsg", resultMsg);
	return objMap;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public boolean updateImImport(BillImImport billImImport,SystemUser user, AuthorityParams authorityParams) throws ManagerException {
            boolean iFlag = false;
			try {
				billImImport.setEditor(user.getLoginName());
				billImImport.setEdittm(new Date());
				billImImport.setUptStatus(STATSU10);
				billImImport.setLocno(user.getLocNo());
				billImImport.setEditorName(user.getUsername());
				int a = billImImportService.modifyById(billImImport);
				if (a < 1) {
				    throw new ManagerException("单据【" + billImImport.getImportNo() + "】已删除或状态已改变，不能进行修改操作！");
				}
				iFlag = true;
			} catch (Exception e) {
			    throw new ManagerException(e);
			}
			return  iFlag;
    }

    @Override
    public int findImpoertNoCount(Map map,AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billImImportService.findImpoertNoCount(map,authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}

    }
    
    @Override
    public List<?> findImportNoByPage(SimplePage page, Map map,AuthorityParams authorityParams)
	    throws ManagerException {
	try {
	    return billImImportService.findImportNoByPage(page, map,authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public Map<String, Object> addBillImImport(BillImImport billImImport)
	    throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	Date date = new Date();
	try {
		String type = billImImport.getBusinessType();
		String supplierNo = billImImport.getSupplierNo();
	    if(type.equals("0")) {
	    	Supplier supplier = new Supplier();
	    	supplier.setSupplierNo(supplierNo);
	    	Supplier s = supplierService.findById(supplier);
	    	if(s != null && s.getSupplierNo() != null) {
	    	} else {
	    		throw new ManagerException("供应商信息不合法！");
	    	}
	    } else if(type.equals("4")) {
	    	Store store = new Store();
	    	store.setStoreNo(supplierNo);
	    	Store s = storeService.findById(store);
	    	if(s != null && s.getStoreNo() != null) {
	    	} else {
	    		throw new ManagerException("供应商信息不合法！");
	    	}
	    } else {
	    	throw new ManagerException("业务类型不合法！");
	    }
		billImImport.setStatus(STATSU10);// 状态10-建单；11-审核；12-验收中；13-结案；14-预约；15-打印预验收；16-取消；17-验收取消中
	    billImImport.setCreatetm(date);
	    billImImport.setEdittm(date);
	    // 自定生成单号
	    String importNo = procCommonService.procGetSheetNo(
		    billImImport.getLocno(), CNumPre.IM_ID_PRE);
	    billImImport.setImportNo(importNo);
	    int a = billImImportService.add(billImImport);
	    if (a < 1) {
		throw new ManagerException("新增时未更新到记录！");
	    }
	    obj.put("returnMsg", true);
	    obj.put("importNo", importNo);
	    obj.put("locno", billImImport.getLocno());
	} catch (Exception e) {
	    throw new ManagerException(e.getMessage());
	}
	return obj;
    }

    @Override
    public Map<String, Object> selectIsReceiptByImportNo(String ImportNo)
	    throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	boolean flag = false;
	String str = "";
	try {
	    String status = billImReceiptService
		    .selectIsReceiptByImportNo(ImportNo);
	    if (StringUtils.isNotBlank(status)) {
		if ("10".equals(status) || "11".equals(status)) {
		    str = "正在验收中，无法结案！";
		} else {
		    flag = true;
		}
	    } else {
		str = "未找到对应的收货单！";
	    }
	    obj.put("returnMsg", str);
	    obj.put("flag", flag);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	return obj;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public Map<String, Object> overImImport(String importNo, String locno,
	    String ownerNo) throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	String flag = "fail";
	String str = "";
	try {

	    // 查询主表下面的所有箱号
	    BillImImport billImImport = new BillImImport();
	    billImImport.setImportNo(importNo);
	    billImImport.setLocno(locno);
	    billImImport.setOwnerNo(ownerNo);
	    BillImImport objBillImImport = billImImportService
		    .selectByPrimaryKey(billImImport);
	    if (!"30".equals(objBillImImport.getStatus())) {// 不是部分验收的状态就不能进行结案操作
		str = "当前状态下，无法操作手工关闭！";
		flag = "warn";
	    } else {
		// 清空通知单的未验收的箱子，并且把状态改为0；
		BillImImportDtlKey billImImportDtlKey = new BillImImportDtlKey();
		billImImportDtlKey.setImportNo(importNo);
		billImImportDtlKey.setLocno(locno);
		billImImportDtlKey.setOwnerNo(ownerNo);
		List<BillImImportDtl> lstBillImImport = billImImportDtlService
			.selectNotCheckBoxNoByDetail(billImImportDtlKey);
		if (null != lstBillImImport && lstBillImImport.size() > 0) {
		    // 更新箱号信息
		    for (BillImImportDtl boxNoObj : lstBillImImport) {
			// 根据箱号把箱信息的入库单号清空
			ConBox conBox = new ConBox();
			conBox.setBoxNo(boxNoObj.getBoxNo());
			conBox.setLocno(locno);
			conBox.setOwnerNo(ownerNo);
			conBox.setStatus("0");// 0未使用
			conBox.setsImportNo("9999");// 9999表示清空
			int b = conBoxService.modifyById(conBox);
			if (b < 1) {
			    throw new ManagerException("清空箱号【"
				    + boxNoObj.getBoxNo() + "】的入库单号信息时未更新到记录！");
			}
		    }

		    // 更改预到货通知单的状态为异常结案-91
		    BillImImport billImImportForUpdate = new BillImImport();
		    billImImportForUpdate.setImportNo(importNo);
		    billImImportForUpdate.setLocno(locno);
		    billImImportForUpdate.setOwnerNo(ownerNo);
		    billImImportForUpdate.setStatus("91");// 状态 11-建单 10-已审核
							  // 12-部分收货 20-收货完成
							  // 30-部分验收 90-结案
							  // 91-异常结案
		    int a = billImImportService
			    .modifyById(billImImportForUpdate);
		    if (a < 1) {
			throw new ManagerException("单号【" + importNo
				+ "】结案时未更新到记录！");
		    }
		    flag = "oversuccess";
		} else {
		    str = "所有箱子已经验收完成，无法手动结案！";
		    flag = "warn";
		}

		// 更新收货单主表的状态为91 异常结案
		// BillImReceiptDtl billImReceiptDtl = new BillImReceiptDtl();
		// billImReceiptDtl.setStatus("91");//修改主表的状态为异常结案
		// billImReceiptDtl.setOwnerNo(ownerNo);
		// billImReceiptDtl.setLocno(locno);
		// billImReceiptDtl.setImportNo(importNo);
		// billImReceiptDtlService.updateStatusByImportNo(billImReceiptDtl);

	    }

	    // List<String> lstBoxNo =
	    // billImReceiptDtlService.selectBoxNoByImportNo(importNo);
	    // if (null != lstBoxNo && lstBoxNo.size() > 0) {
	    // //没有入库的箱号
	    // List<String> lstBoxUpt = new ArrayList<String>();
	    //
	    // //查询主表下面的所有箱号
	    // BillImImportDtlKey billImImportDtlKey = new BillImImportDtlKey();
	    // List<BillImImportDtl> lstBillImImport = billImImportDtlService
	    // .selectBoxNoListByDetail(billImImportDtlKey);
	    // Iterator<BillImImportDtl> itAll = lstBillImImport.iterator();
	    //
	    // Set<String> boxNoMany = new HashSet<String>(lstBoxNo);
	    // while (itAll.hasNext()) {
	    // BillImImportDtl vo = itAll.next();
	    // String boxNo = vo.getBoxNo();
	    // if (!boxNoMany.contains(boxNo)) {
	    // lstBoxUpt.add(boxNo);
	    // }
	    // }
	    // //更新箱号信息
	    // for (String boxNo : lstBoxUpt) {
	    // //根据箱号把箱信息的入库单号清空
	    // ConBox conBox = new ConBox();
	    // conBox.setBoxNo(boxNo);
	    // conBox.setLocno(locno);
	    // conBox.setOwnerNo(ownerNo);
	    // conBox.setStatus("0");//0未使用
	    // conBox.setsImportNo("9999");//9999表示清空
	    // int b = conBoxService.modifyById(conBox);
	    // if (b < 1) {
	    // throw new ManagerException("清空箱号【" + boxNo + "】的入库单号信息时未更新到记录！");
	    // }
	    // }
	    //
	    // BillImImport billImImport = new BillImImport();
	    // billImImport.setImportNo(importNo);
	    // billImImport.setLocno(locno);
	    // billImImport.setOwnerNo(ownerNo);
	    // billImImport.setStatus(" 91");//状态 11-建单 10-已审核 12-部分收货 20-收货完成
	    // 30-部分验收 90-结案 91-异常结案
	    //
	    // int a = billImImportService.modifyById(billImImport);
	    // if (a < 1) {
	    // throw new ManagerException("单号【" + importNo + "】结案时未更新到记录！");
	    // }
	    //
	    // m = "success";
	    // } else {
	    // m = "warn";
	    // }

	    obj.put("returnMsg", str);
	    obj.put("flag", flag);

	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	return obj;
    }

    @Override
    public List<Supplier> findSupplierNo(BillImImport imp,AuthorityParams authorityParams)
	    throws ManagerException {
	try {
	    return billImImportService.findSupplierNo(imp,authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public int overFlocByDif(String keyStr, String locno, String ownerNo,
	    String auditor,String auditorName,AuthorityParams authorityParams) throws ManagerException {
	int count = 0;
	boolean canFloc = false;
	Date edittm = new Date();
	if (StringUtils.isNotBlank(keyStr)) {
	    String[] strs = keyStr.split(",");
	    BillImImport params = null;
	    BillImImport billImImport = null;
	    for (String importNo : strs) {
		try {
		    params = new BillImImport();
		    params.setLocno(locno);
		    params.setOwnerNo(ownerNo);
		    params.setImportNo(importNo);

		    billImImport = billImImportService
			    .selectByPrimaryKey(params);
		    if (!billImImport.getStatus().equals(STATSU30)) {
			throw new ManagerException("预到货通知单【" + importNo
				+ "】为非差异验收，不允许关闭。");
		    }

		    canFloc = billImImportService
			    .isCanOverFlocByReceipt(params);
		    if (!canFloc) {
			throw new ManagerException("预到货通知单【" + importNo
				+ "】验收流程未完成，不允许关闭。");
		    }

		    canFloc = billImImportService.isCanOverFlocByDivide(params);
		    if (!canFloc) {
			throw new ManagerException("预到货通知单【" + importNo
				+ "】分货流程未完成，不允许关闭。");
		    }

		    canFloc = billImImportService.isCanOverFlocByCheck(params);
		    if (!canFloc) {
			throw new ManagerException("预到货通知单【" + importNo
				+ "】存在建单状态验收单，不允许关闭。");
		    }

		    // 更新预到货通知单为手动关闭
		    if (canFloc) {
			params.setStatus(STATSU91);
			params.setEditor(auditor);
			params.setEdittm(edittm);
			params.setEditorName(auditorName);
			count += billImImportService.modifyById(params);
		    }

		} catch (Exception e) {
		    throw new ManagerException(e.getMessage());
		}
	    }
	}
	return count;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void bathUpload(String locno, String ownerNo,String importNoStr, AuthorityParams authorityParams) throws ManagerException {
		try {
			if (StringUtils.isEmpty(importNoStr) || StringUtils.isEmpty(locno)) {
				throw new ManagerException("分批上传参数非法！");
			}
			String[] str = importNoStr.split(",");
			for (String importNo : str) {
				BillImImport params = new BillImImport();
				params.setLocno(locno);
				params.setOwnerNo(ownerNo);
				params.setImportNo(importNo);

				BillImImport    billImImport = billImImportService.selectByPrimaryKey(params);
				if (!billImImport.getStatus().equals(STATSU30)) {
					throw new ManagerException("预到货通知单【" + importNo
						+ "】为非差异验收，不允许分批上传！");
				}
				// 继续定位
				procCommonService.bathUpload(locno, importNo);
			}
		} catch (ServiceException e) {
			// logger.error(e.getMessage());
			throw new ManagerException(e.getMessage());
		}

	}
	
	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billImImportService.findSumQty(params, authorityParams);
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public boolean toBillimReceipt(List<BillImImport> listIm, SystemUser user)
			throws ManagerException {
		Date date=new Date();
		try{
			BillImReceipt receipt = null;
			//创建收货单
			receipt=new BillImReceipt();
			receipt.setStatus(STATSU20);
			receipt.setOwnerNo(listIm.get(0).getOwnerNo());
			receipt.setReceiptStartDate(date);
			receipt.setReceiptEndDate(date);
			receipt.setRecivedate(date);
			receipt.setReceiptName(user.getUsername());
			receipt.setReceiptWorker(user.getLoginName());
			receipt.setCreatetm(date);
			receipt.setCreator(user.getLoginName());
			receipt.setCreatorName(user.getUsername());
			receipt.setEdittm(date);
			receipt.setEditorName(user.getUsername());
			receipt.setEditor(user.getLoginName());
			receipt.setLocno(user.getLocNo());
			String receiptNo = procCommonService.procGetSheetNo(receipt.getLocno(), 
					CNumPre.IM_RECEDE_PRE);
			receipt.setReceiptNo(receiptNo);
			BigDecimal sumQty=new BigDecimal(0);
			BigDecimal boxNoQty=new BigDecimal(0);
			Map<String,Object> params=new HashMap<String, Object>();
			BillImImportDtl dtl=null;
			int rowId=0;
			for(BillImImport billImImport:listIm){
				
				params.clear();
				//增加收货单明细
				params.put("locno", billImImport.getLocno());
				params.put("importNo", billImImport.getImportNo());
				params.put("ownerNo", billImImport.getOwnerNo());
				dtl=new BillImImportDtl(); 
				List<BillImImportDtl> listImDtl= billImImportDtlService.findByBiz(dtl, params);
				billImReceiptService.addReceiptDtl(receipt, listImDtl, billImImport.getQuality(),rowId);
				rowId+=listImDtl.size();
				//更新收货数量
				BillImImportDtl billImImportDtl=new BillImImportDtl();
				billImImportDtl.setLocno(billImImport.getLocno());
				billImImportDtl.setImportNo(billImImport.getImportNo());
				billImImportDtlService.updateAllReceiptQty(billImImportDtl);
				//更新预发货通知单状态
				billImImport.setEditor(user.getLoginName());
				billImImport.setEdittm(date);
				billImImport.setEditorName(user.getUsername());
				billImImport.setStatus(STATSU20);
				billImImportService.modifyById(billImImport);
				
				//----------------------计算箱数和商品数
				sumQty=sumQty.add(billImImport.getSumQty());
				boxNoQty=boxNoQty.add(billImImport.getBoxNoQty());

			}
			receipt.setAudittm(date);
			receipt.setAuditorName(user.getUsername());
			receipt.setAuditor(user.getLoginName());
			receipt.setReceiptqty(sumQty);
			receipt.setBoxqty(boxNoQty);
			billImReceiptService.add(receipt);
		}catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
		return false;
	}
	
}