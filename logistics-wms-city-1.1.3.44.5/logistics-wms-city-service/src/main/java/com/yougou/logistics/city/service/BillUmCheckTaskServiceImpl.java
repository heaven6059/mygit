package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillUmCheckTaskDtlMapper;
import com.yougou.logistics.city.dal.database.BillUmCheckTaskMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Service("billUmCheckTaskService")
class BillUmCheckTaskServiceImpl extends BaseCrudServiceImpl implements BillUmCheckTaskService {
	
	@Resource
    private BillUmCheckTaskMapper billUmCheckTaskMapper;
	
	@Resource
    private BillUmCheckTaskDtlMapper billUmCheckTaskDtlMapper;
	
	@Resource
	private BillUmUntreadMapper billUmUntreadMapper;
	
	@Resource
	private BillUmUntreadDtlMapper billUmUntreadDtlMapper;

    @Resource
    private ProcCommonService procCommonService;
    
    private final static String STATUS10 = "10";
    
    @Override
    public BaseCrudMapper init() {
        return billUmCheckTaskMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveUmCheckTask(BillUmCheckTask umCheckTask,List<BillUmUntread> untreadList) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(untreadList)||umCheckTask == null){
				throw new ServiceException("新增的店退仓单参数为空!");
			}
			//判断退仓单状态是否改变或者是否被删除
			for (BillUmUntread billUmUntread : untreadList) {
				BillUmUntreadKey billUmUntreadKey = new BillUmUntreadKey();
				billUmUntreadKey.setLocno(umCheckTask.getLocno());
				billUmUntreadKey.setOwnerNo(billUmUntread.getOwnerNo());
				billUmUntreadKey.setUntreadNo(billUmUntread.getUntreadNo());
				int exitsFlag = billUmUntreadMapper.judgeObjIsExist(billUmUntreadKey, "11");
				if (0 == exitsFlag){
					throw new ServiceException("单据:" + billUmUntread.getUntreadNo() +"已删除或者状态已改变！");
				}
			}
			
			//新增主档信息
			String checkTaskNo = procCommonService.procGetSheetNo(umCheckTask.getLocno(), CNumPre.UM_CHECK_TASK_NO_PRE);
			umCheckTask.setCheckTaskNo(checkTaskNo);
			umCheckTask.setStatus(STATUS10);
			umCheckTask.setOwnerNo(untreadList.get(0).getOwnerNo());
			int count = billUmCheckTaskMapper.insertSelective(umCheckTask);
			if(count < 1){
				throw new ServiceException("新增退仓验收任务单主档信息失败!");
			}
			//新增明细信息
			int dcount = billUmCheckTaskDtlMapper.insertUmCheckTaskDtl(umCheckTask, untreadList);
			if(dcount < 1){
				throw new ServiceException("新增退仓验收任务单明细信息失败!");
			}
			//保存退仓验收任务，更新来源店退仓单状态为15-已分配任务
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", umCheckTask.getLocno());
			params.put("ownerNo", umCheckTask.getOwnerNo());
			int ucount = billUmUntreadMapper.updateUntreadStatus4CheckTask(params, untreadList);
			if(ucount < 1){
				throw new ServiceException("更新店退仓通知单状态为15-<已分配任务>失败!");
			}
			//更新明细的收货数量
			int ucount2 = billUmUntreadDtlMapper.updateUntreadReceiptQtyByUntreadNo(params, untreadList);
			if(ucount2 < 1){
				throw new ServiceException("更新店退仓通知单明细收货数量失败!");
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteUmCheckTask(List<BillUmCheckTask> taskList) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(taskList)){
				throw new ServiceException("参数为空!");
			}
			
			//先回滚状态
			BillUmCheckTask umCheckTask = taskList.get(0);
			billUmCheckTaskMapper.updateRollbackUntreadStatus4CheckTask(umCheckTask, taskList);
//			if(ucount < 1){
//				throw new ServiceException("回滚店退仓通知单状态<已审核>失败!");
//			}
			//回滚店退仓明细数量
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", umCheckTask.getLocno());
			params.put("ownerNo", umCheckTask.getOwnerNo());
			billUmUntreadDtlMapper.updateUntreadReceiptQtyByCheckTaskNo(params, taskList);
//			if(ucount2 < 1){
//				throw new ServiceException("更新店退仓通知单明细收货数量失败!");
//			}
			
			//删除主档和明细
			for (BillUmCheckTask billUmCheckTask : taskList) {
				billUmCheckTask.setCheckStatus(STATUS10);
				billUmCheckTaskDtlMapper.deleteUmCheckTaskDtlByCheckTaskNo(billUmCheckTask);
				int count = billUmCheckTaskMapper.deleteByPrimarayKeyForModel(billUmCheckTask);
				if(count < 1){
					throw new ServiceException("单据 :"+billUmCheckTask.getCheckTaskNo()+"已删除或状态已改变");
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

    @Override
    public Map<String, Object> selectSumQty(Map<String, Object> params,
            AuthorityParams authorityParams) throws ServiceException {
        try{
            return this.billUmCheckTaskMapper.selectSumQty(params,authorityParams);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
}