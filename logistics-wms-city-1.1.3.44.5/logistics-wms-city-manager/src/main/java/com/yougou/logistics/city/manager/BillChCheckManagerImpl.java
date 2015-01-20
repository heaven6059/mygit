package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.enums.BillChCheckDirectStautsEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChCheckDtlKey;
import com.yougou.logistics.city.common.model.BillChCheckKey;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillChCheckDirectService;
import com.yougou.logistics.city.service.BillChCheckDtlService;
import com.yougou.logistics.city.service.BillChCheckService;
import com.yougou.logistics.city.service.BillChPlanService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
@Service("billChCheckManager")
class BillChCheckManagerImpl extends BaseCrudManagerImpl implements BillChCheckManager {
	@Resource
	private BillChCheckService billChCheckService;
	
	@Resource
	private BillChCheckDtlService billChCheckdtlService;

	@Resource
	private BillChCheckDirectService billChCheckDirectService;
	
	@Resource
	private BillChPlanService billChPlanService;
	@Override
	public BaseCrudService init() {
		return billChCheckService;
	}

	@Override
	public void distributionAssignNoBatch(String locno, String checkNos, String assignNo,int systemId,int areaSystemId) throws ManagerException {
		try {
			billChCheckService.distributionAssignNoBatch(locno, checkNos, assignNo,systemId,areaSystemId);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void createBillChCheck(BillChCheckDirectDto dto, String check_date, Integer stockCount, Integer cellCount,
			String creator,String creatorName) throws ManagerException {
		try {
			billChCheckService.createBillChCheck(dto, check_date, stockCount, cellCount, creator,creatorName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillChCheckDto> selectChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billChCheckService.selectChCheck(map, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public void chCheck(String checkNos, String locno, String curUser,String curUsername) throws ManagerException {
		try {
			billChCheckService.chCheck(checkNos, locno, curUser,curUsername);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectChCheckSumQty(
			Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billChCheckService.selectChCheckSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}

	@Override
	public String hasBegunCheck(String locno, String checkNos)
			throws ManagerException {
		StringBuilder sb = new StringBuilder();
		try {
			String [] checkNoArray = checkNos.split("\\|");
			Map<String, Object> params = new HashMap<String, Object>();
			List<BillChCheckDtl> list;
			params.put("locno", locno);
			for(String checkNo:checkNoArray){
				params.put("checkNo", checkNo);
				list = billChCheckdtlService.findByBiz(null, params);
				for(BillChCheckDtl dtl : list){
					if(dtl.getCheckQty() != null && dtl.getCheckQty().intValue() > 0){
						sb.append("【"+checkNo+"】开始盘点.<br>");
						break;
					}
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return sb.toString();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String restoreCheck(String locno, String values)
			throws ManagerException {
		try {
			String [] array = values.split("\\|");
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> modifyCellParams = new HashMap<String, Object>();
			modifyCellParams.put("locno", locno);
			modifyCellParams.put("checkStatus", CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus());
			modifyCellParams.put("sourceCheckStatus", CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus());
			List<BillChCheckDtl> list;
			Map<String, Object> checkDtlMap = null;
			List<BillChCheckDirect> directs;
			params.put("locno", locno);
			BillChCheckDtlKey chCheckDtlKey = new BillChCheckDtlKey();
			chCheckDtlKey.setLocno(locno);
			BillChCheckKey chCheckKey = new BillChCheckKey();
			chCheckKey.setLocno(locno);
			BillChPlan billChPlan = new BillChPlan();
			billChPlan.setLocno(locno);
			for(String noStr:array){
				String [] noArray = null;
				noArray = noStr.split("_");
				String checkNo = noArray[0];
				String planNo = noArray[1];
				params.put("checkNo", checkNo);
				list = billChCheckdtlService.findByBiz(null, params);//查询盘点明细
				checkDtlMap = new HashMap<String, Object>();
				for(BillChCheckDtl dtl : list){
					String str = dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo();
					checkDtlMap.put(str, str);
				}
				//1、删除盘点单
				chCheckKey.setCheckNo(checkNo);
				int idx = billChCheckService.deleteById(chCheckKey);
				if(idx <= 0){
					throw new ManagerException("盘点单【"+checkNo+"】已不存在!");
				}
				//jys RF发的单撤销时要将储位盘点状态改为可用
				modifyCellParams.put("checkNo", checkNo);
				billChCheckdtlService.modityCellCheckStatusByCheckDtl(modifyCellParams);
				//2、删除盘点明细
				chCheckDtlKey.setCheckNo(checkNo);
				billChCheckdtlService.deleteById(chCheckDtlKey);
				//3、修改定位明细状态为10
				directs = billChCheckDirectService.findByBiz(null, params);//查询定位明细
				for(BillChCheckDirect dtl:directs){
					String str = dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo();
					if(checkDtlMap.get(str) != null){
						dtl.setSourceStatus(BillChCheckDirectStautsEnums.COMPLETE.getValue());
						dtl.setStatus(BillChCheckDirectStautsEnums.CREATE.getValue());
						billChCheckDirectService.modifyById(dtl);
					}
				}
				//4、修改盘点计划单状态为15
				billChPlan.setPlanNo(planNo);
				billChPlan.setStatus(BillChPlanStatusEnums.START.getValue());
				billChPlan.setSourceStatus(BillChPlanStatusEnums.SEND.getValue());
				billChPlanService.modifyById(billChPlan);
				
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return null;
	}

	@Override
	public SumUtilMap<String, Object> selectChCheckPlanSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billChCheckService.selectChCheckPlanSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}