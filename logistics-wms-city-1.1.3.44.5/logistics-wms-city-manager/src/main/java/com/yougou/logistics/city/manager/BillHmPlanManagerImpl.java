package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.enums.BillHmPlanBusinessTypeEnums;
import com.yougou.logistics.city.common.enums.BillWmPlanStatusEnums;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.model.BillHmPlanKey;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillHmPlanDtlService;
import com.yougou.logistics.city.service.BillHmPlanService;
import com.yougou.logistics.city.service.BillWmPlanService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@Service("billHmPlanManager")
class BillHmPlanManagerImpl extends BaseCrudManagerImpl implements BillHmPlanManager {

	@Log
	private Logger log;

	@Resource
	private ProcCommonService procCommonService;

	@Resource
	private BillHmPlanService billHmPlanService;

	@Resource
	private BillHmPlanDtlService billHmPlanDtlService;
	@Resource
	private BillWmPlanService billWmPlanService;

	private static final String STATUS10 = "10";

	@Override
	public BaseCrudService init() {
		return billHmPlanService;
	}

	public String saveMain(BillHmPlan billHmPlan,SystemUser user) throws ManagerException {
		String planNo = "";
		try {
			Date date = new Date();
			billHmPlan.setLocno(user.getLocNo());
			billHmPlan.setEditor(user.getLoginName());
			billHmPlan.setEdittm(date);
			billHmPlan.setEditorName(user.getUsername());
			
			if (StringUtils.isNotBlank(billHmPlan.getPlanNo())) {
				billHmPlan.setUpdStatus("10");
				int result = billHmPlanService.modifyById(billHmPlan);
				if (result < 1) {
					//throw new ManagerException("修改移库单时未更新到记录！");
					throw new ManagerException("单据"+billHmPlan.getPlanNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
				planNo = billHmPlan.getPlanNo();
			} else {
				//新增
				billHmPlan.setCreator(user.getLoginName());
				billHmPlan.setCreatetm(date);
				billHmPlan.setCreatorName(user.getUsername());
				//自定生成单号
				planNo = procCommonService.procGetSheetNo(billHmPlan.getLocno(), CNumPre.Hm_Move_PRE);
				billHmPlan.setPlanNo(planNo);
				billHmPlan.setStatus(STATUS10);
				int result = billHmPlanService.add(billHmPlan);
				if (result < 1) {
					throw new ManagerException("新增时未更新到记录！");
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return planNo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> BillHmPlan addBillHmPlan(BillHmPlan billHmPlan, Map<CommonOperatorEnum, List<ModelType>> params,SystemUser user)
			throws ManagerException {
		try {
			
			/****************新增移单明细信息*******************/
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);//新增
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);//删除
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新
			
			
			
			//检查主档单据是否存在或为新建状态
			BillHmPlanKey key = new BillHmPlanKey();
			key.setLocno(billHmPlan.getLocno());
			key.setPlanNo(billHmPlan.getPlanNo());
			
			BillHmPlan resultBillHmPlan = (BillHmPlan) billHmPlanService.findById(key);
			if(resultBillHmPlan == null || !"10".equals(resultBillHmPlan.getStatus())){
				throw new ManagerException("单据"+key.getPlanNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}
			
			billHmPlan.setEditor(user.getLoginName());
			billHmPlan.setEditorName(user.getUsername());
			billHmPlan.setEdittm(new Date());
			billHmPlanService.modifyById(billHmPlan);
			//新增移库计划单明细
			if (CommonUtil.hasValue(addList)) {

				//查询最大的Pid,作为主键
				BillHmPlanDtl keyObj = new BillHmPlanDtl();
				keyObj.setPlanNo(billHmPlan.getPlanNo());
				keyObj.setLocno(billHmPlan.getLocno());
				keyObj.setOwnerNo(billHmPlan.getOwnerNo());
				long pidNum = (long) billHmPlanDtlService.selectMaxPid(keyObj);

				//插入移库单明细
				for (ModelType modelType : addList) {
					if (modelType instanceof BillHmPlanDtl) {

						//写入移库单明细信息
						BillHmPlanDtl b = (BillHmPlanDtl) modelType;
						b.setLocno(billHmPlan.getLocno());
						b.setOwnerNo(billHmPlan.getOwnerNo());
						b.setPlanNo(billHmPlan.getPlanNo());
						b.setMoveDate(new Date());
						b.setRowId(++pidNum);
						int result = billHmPlanDtlService.add(b);
						if (result < 1) {
							throw new ManagerException("插入移库单明细记录时未更新到记录！");
						}
					}
				}
			}

			//更新移库计划单
			if (CommonUtil.hasValue(uptList)) {
				//插入移库单明细
				for (ModelType modelType : uptList) {
					if (modelType instanceof BillHmPlanDtl) {
						BillHmPlanDtl b = (BillHmPlanDtl) modelType;
						int result = billHmPlanDtlService.modifyById(b);
						if (result < 1) {
							throw new ManagerException("更新移库单明细信息时未更新到记录！");
						}
					}
				}
			}

			//删除移库计划单明细
			if (CommonUtil.hasValue(delList)) {
				//插入移库单明细
				for (ModelType modelType : delList) {
					if (modelType instanceof BillHmPlanDtl) {
						BillHmPlanDtl b = (BillHmPlanDtl) modelType;
						//删除预到货通知单明细信息，根据箱号
						BillHmPlanDtl delParamerKey = new BillHmPlanDtl();
						delParamerKey.setPlanNo(b.getPlanNo());
						delParamerKey.setLocno(b.getLocno());
						delParamerKey.setOwnerNo(b.getOwnerNo());
						delParamerKey.setItemNo(b.getItemNo());
						delParamerKey.setItemId(b.getItemId());
						delParamerKey.setSizeNo(b.getSizeNo());
						delParamerKey.setRowId(b.getRowId());
						int result = billHmPlanDtlService.deleteById(delParamerKey);
						if (result < 1) {
							throw new ManagerException("删除移库单明细信息时未更新到记录！");
						}
					}
				}
			}
			
			//查询明细是否重复
			Map<String, Object> findDupParams = new HashMap<String, Object>();
			findDupParams.put("locno", billHmPlan.getLocno());
			findDupParams.put("ownerNo", billHmPlan.getOwnerNo());
			findDupParams.put("planNo", billHmPlan.getPlanNo());
			List<BillHmPlanDtl> dupList=billHmPlanDtlService.findDuplicateRecord(findDupParams);
			if(dupList.size()>0){
				StringBuilder msg = new StringBuilder();
				for(BillHmPlanDtl dtl : dupList){
					msg.append("<br>储位:").append(dtl.getsCellNo()).append("<br>商品:").append(dtl.getItemNo()).append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
				}
				msg.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ManagerException(msg.toString());
			}
			
			
			return billHmPlan;
		}catch (ManagerException e) {
			log.error(e.getMessage(),e);
			throw new ManagerException(e.getMessage());
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public boolean deleteBillHmPlan(List<BillHmPlan> listBillHmPlan) throws ManagerException {
		try {
			BillHmPlan oldBillHmPlan=null;
			BillHmPlanKey billHmPlanKey=new BillHmPlanKey();
			String businessType;
			String sourceNo;
			for (BillHmPlan b : listBillHmPlan) {
				
				billHmPlanKey.setLocno(b.getLocno());
				billHmPlanKey.setPlanNo(b.getPlanNo());
				oldBillHmPlan=(BillHmPlan) billHmPlanService.findById(billHmPlanKey);
				if(oldBillHmPlan == null || !"10".equals(oldBillHmPlan.getStatus())){
					throw new ManagerException("单据"+b.getPlanNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
				businessType=oldBillHmPlan.getBusinessType();
				sourceNo=oldBillHmPlan.getSourceNo();
				//删除移库计划时，如果业务类型是退厂计划，则回滚退厂计划状态为已审核
				if(BillHmPlanBusinessTypeEnums.BUSINESSTYPE1.getStatus().equals(businessType)){
					BillWmPlan updateBillWmPlan=new BillWmPlan();
					updateBillWmPlan.setLocno(b.getLocno());
					updateBillWmPlan.setOwnerNo(b.getOwnerNo());
					updateBillWmPlan.setPlanNo(sourceNo);
					updateBillWmPlan.setStatus(BillWmPlanStatusEnums.STATUS11.getStatus());
					billWmPlanService.modifyById(updateBillWmPlan);
				}
				
				
				//删除主表
				b.setUpdStatus("10");
				int result = billHmPlanService.deleteById(b);
				if (result < 1) {
					throw new ManagerException("单据"+b.getPlanNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				} else {
					//删除明细表
					BillHmPlanDtl dtl = new BillHmPlanDtl();
					dtl.setLocno(b.getLocno());
					dtl.setOwnerNo(b.getOwnerNo());
					dtl.setPlanNo(b.getPlanNo());
					billHmPlanDtlService.deleteById(dtl);
				}
			}
			return true;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void audit(String planNo, SystemUser user) throws ManagerException {
		try {
			billHmPlanService.audit(planNo, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void cancelBillHmPlan(List<BillHmPlan> listPlans, SystemUser user) throws ManagerException {
		try {
			billHmPlanService.cancelBillHmPlan(listPlans, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}