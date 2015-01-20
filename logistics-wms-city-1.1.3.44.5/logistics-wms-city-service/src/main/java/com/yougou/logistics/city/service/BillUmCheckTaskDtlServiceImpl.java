package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskDtl;
import com.yougou.logistics.city.common.model.BillUmCheckTaskKey;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillUmCheckTaskDtlMapper;
import com.yougou.logistics.city.dal.database.BillUmCheckTaskMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
@Service("billUmCheckTaskDtlService")
class BillUmCheckTaskDtlServiceImpl extends BaseCrudServiceImpl implements BillUmCheckTaskDtlService {
	
	@Resource
	private BillUmCheckTaskDtlMapper billUmCheckTaskDtlMapper;
	
	@Resource
	private BillUmCheckTaskMapper billUmCheckTaskMapper;
	
	@Resource
	private BillUmUntreadMapper billUmUntreadMapper;
	
	@Resource
	private BillUmUntreadDtlMapper billUmUntreadDtlMapper;
	
	private final static String STATUS10 = "10";

	@Override
	public BaseCrudMapper init() {
		return billUmCheckTaskDtlMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckTaskDtlMapper.selectSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public List<BillUmCheckTaskDtl> findUntreadNo4CheckTaskDtl(Map<String, Object> params) throws ServiceException {
		try {
			return billUmCheckTaskDtlMapper.selectUntreadNo4CheckTaskDtl(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<Item> findItem4CheckTask(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckTaskDtlMapper.selectItem4CheckTask(params, page, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findItemCount4CheckTask(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckTaskDtlMapper.selectItemCount4CheckTask(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveCheckQty4itemQty(Map<String, Object> params) throws ServiceException {
		try {
			billUmCheckTaskDtlMapper.saveCheckQty4itemQty(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveUmCheckTaskDtl(List<BillUmCheckTaskDtl> insertList, List<BillUmCheckTaskDtl> updateList,
			List<BillUmCheckTaskDtl> deleteList, BillUmCheckTask check) throws ServiceException {
		try{
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setOwnerNo(check.getOwnerNo());
			billUmCheckTaskKey.setLocno(check.getLocno());
			billUmCheckTaskKey.setCheckTaskNo(check.getCheckTaskNo());
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskMapper.selectByPrimaryKey(billUmCheckTaskKey);
			if (null == billUmCheckTask){
				throw new ServiceException("单据: "+check.getCheckTaskNo() +"已删除");
			}
			if(!"10".equals(billUmCheckTask.getStatus())){
				throw new ServiceException("单据: "+check.getCheckTaskNo() +"状态已改变,不能进行当前操作");
			}
			//删除行
			if(CommonUtil.hasValue(deleteList)){
				for (BillUmCheckTaskDtl dtl : deleteList) {
					dtl.setLocno(check.getLocno());
					dtl.setOwnerNo(check.getOwnerNo());
					billUmCheckTaskDtlMapper.deleteByPrimarayKeyForModel(dtl);
				}
			}
			
			//修改的行
			if(CommonUtil.hasValue(updateList)){
				for (BillUmCheckTaskDtl dtl : updateList) {
					dtl.setLocno(check.getLocno());
					dtl.setOwnerNo(check.getOwnerNo());
					billUmCheckTaskDtlMapper.updateByPrimaryKeySelective(dtl);
				}
			}
			
			//新增行
			if (CommonUtil.hasValue(insertList)) {
				int pidNum =  billUmCheckTaskDtlMapper.selectMaxPid(check);
				for (BillUmCheckTaskDtl taskDtl : insertList) {
					taskDtl.setLocno(check.getLocno());
					taskDtl.setOwnerNo(check.getOwnerNo());
					taskDtl.setCheckTaskNo(check.getCheckTaskNo());
					taskDtl.setChecktm(new Date());
					taskDtl.setCheckWorker(check.getCreator());
					taskDtl.setCheckWorkerName(check.getCreatorName());
					taskDtl.setRowId(++pidNum);
					taskDtl.setStatus(STATUS10);
					taskDtl.setPackQty(1);
					int result = billUmCheckTaskDtlMapper.insertSelective(taskDtl);
					if (result < 1) {
						throw new ManagerException("插入退仓验收任务明细记录时未更新到记录！");
					}
				}
			}
			
			billUmCheckTaskMapper.updateByPrimaryKeySelective(check);
			//验证是否添加重复的商品
			List<BillUmCheckTaskDtl> dupList=billUmCheckTaskDtlMapper.selectDuplicateCheckTask(check);
			if(dupList.size()>0){
				StringBuilder msg = new StringBuilder();
				for(BillUmCheckTaskDtl dtl : dupList){
					msg.append("<br>店退仓单:").append(dtl.getUntreadNo())
					.append("<br>客户名称:").append(dtl.getStoreName())
					.append("<br>商品:").append(dtl.getItemNo())
					.append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
					break;
				}
				msg.append("<div style='text-align:center;color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;不允许出现相同数据!</div>");
				throw new ManagerException(msg.toString());
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void delUntreadByCheckTask(List<BillUmCheckTaskDtl> taskDtlList) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(taskDtlList)){
				throw new ServiceException("参数为空!");
			}
			//循环删除明细
			for (BillUmCheckTaskDtl taskDtl : taskDtlList) {
				int count =billUmCheckTaskDtlMapper.deleteUmCheckTaskDtlByUntreadNo(taskDtl);
				if(count < 1){
					throw new ManagerException(taskDtl.getUntreadNo()+"按单删除店退仓单失败！");
				}
				
				
				//更新店退仓状态为已审核
				BillUmUntread umUntread = new BillUmUntread();
				umUntread.setLocno(taskDtl.getLocno());
				umUntread.setOwnerNo(taskDtl.getOwnerNo());
				umUntread.setUntreadNo(taskDtl.getUntreadNo());
				int ucount = billUmUntreadMapper.updateUntread2Status11(umUntread);
				if(ucount < 1){
					throw new ManagerException(taskDtl.getUntreadNo()+"更新状态为<已审核>失败！");
				}
				//更新明细状态
				billUmUntreadDtlMapper.updateUntreadDtlReceiptQty(umUntread);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void updateCheckQtyToZero(List<BillUmCheckTaskDtl> taskDtlList) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(taskDtlList)){
				throw new ServiceException("参数为空!");
			}
			//循环商品置0
			for (BillUmCheckTaskDtl taskDtl : taskDtlList) {
				taskDtl.setCheckQty(new BigDecimal(0));
				billUmCheckTaskDtlMapper.updateByPrimaryKeySelective(taskDtl);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}