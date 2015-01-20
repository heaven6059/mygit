package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillLocateRule;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.BillLocateRuleQuery;
import com.yougou.logistics.city.service.BillLocateRuleDtlService;
import com.yougou.logistics.city.service.BillLocateRuleService;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 储位锁定策略
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
@Service("billLocateRuleManager")
class BillLocateRuleManagerImpl extends BaseCrudManagerImpl implements BillLocateRuleManager {
	
    @Resource
    private BillLocateRuleService billLocateRuleService;
    
    @Resource
    private BillLocateRuleDtlService billLocateRuleDtlService;

    @Override
    public BaseCrudService init() {
        return billLocateRuleService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int saveBillLocateRule(BillLocateRuleQuery query) throws ManagerException {
		try {
			
			BillLocateRule billLocateRule = query.getBillLocateRule();
			int operStatus = query.getOperStatus();
			List<BillLocateRuleDtl> listInserteds = query.getListInserteds();
			List<BillLocateRuleDtl> listdeleteds = query.getListdeleteds();
			
			if(billLocateRule==null){
				throw new ManagerException("对象为空！");
			}
			
			//新增/修改储位策略主表信息
			int result = 0;
			if(operStatus > 0){
				result = billLocateRuleService.modifyById(billLocateRule);
				if(result < 1){
					 throw new ManagerException("修改储位策略时未更新到记录！");
				}
			}else{
				result = billLocateRuleService.add(billLocateRule);
				if (result < 1) {
					throw new ManagerException("新增储位策略时未更新到记录！");
				}
			}
			
			//循环添加明细
			if(CommonUtil.hasValue(listInserteds)){
				for (BillLocateRuleDtl dtl : listInserteds) {
					dtl.setLocno(billLocateRule.getLocno());
					dtl.setRuleNo(billLocateRule.getRuleNo());
					int count = billLocateRuleDtlService.add(dtl);
					if (count < 1) {
						throw new ManagerException("新增储位策略明细时未更新到记录！");
					}
				}
			}
			
			//循环删除明细
			if(CommonUtil.hasValue(listdeleteds)){
				for (BillLocateRuleDtl dtl : listdeleteds) {
					dtl.setLocno(billLocateRule.getLocno());
					int count = billLocateRuleDtlService.deleteById(dtl);
					if (count < 1) {
						throw new ManagerException("删除储位策略明细时未更新到记录！");
					}
				}
			}
			
//			//新增储位策略明细
//			int count = 0;
//			if(CommonUtil.hasValue(listCategorys)){
//				
//				//删除储位策略锁定明细
//				BillLocateRuleDtl entity = new BillLocateRuleDtl();
//				entity.setLocno(billLocateRule.getLocno());
//				entity.setRuleNo(billLocateRule.getRuleNo());
//				billLocateRuleDtlService.deleteById(entity);
//				
//				//循环添加明细
//				for (Category category : listCategorys) {
//					BillLocateRuleDtl ruleDtl = new BillLocateRuleDtl();
//					ruleDtl.setLocno(billLocateRule.getLocno());
//					ruleDtl.setRuleCatename(category.getCateName());
//					ruleDtl.setRuleCateno(category.getCateNo());
//					ruleDtl.setRuleNo(billLocateRule.getRuleNo());
//					count += billLocateRuleDtlService.add(ruleDtl);
//				}
//				
//				if (count < 1) {
//					throw new ManagerException("新增储位策略明细时未更新到记录！");
//				}
//			}
			return result;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBillLocateRule(List<BillLocateRule> listBillLocateRules) throws ManagerException {
		try{
			int count = 0;
			if(CommonUtil.hasValue(listBillLocateRules)){
				for (BillLocateRule b : listBillLocateRules) {
					int result = billLocateRuleService.deleteById(b);
					if(result < 1){
						throw new ManagerException("删除储位策略失败!");
					}
					
					BillLocateRuleDtl ruleDtl = new BillLocateRuleDtl();
					ruleDtl.setLocno(b.getLocno());
					ruleDtl.setRuleNo(b.getRuleNo());
					billLocateRuleDtlService.deleteById(ruleDtl);
					count += result;
				}
			}
			return count;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
    
	
}