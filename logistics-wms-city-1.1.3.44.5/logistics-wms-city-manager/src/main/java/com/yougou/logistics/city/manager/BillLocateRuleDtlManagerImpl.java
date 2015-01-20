package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillLocateRuleDtlService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
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
@Service("billLocateRuleDtlManager")
class BillLocateRuleDtlManagerImpl extends BaseCrudManagerImpl implements BillLocateRuleDtlManager {
	
    @Resource
    private BillLocateRuleDtlService billLocateRuleDtlService;

    @Override
    public BaseCrudService init() {
        return billLocateRuleDtlService;
    }

	@Override
	public int findCategoryFilterCount(BillLocateRuleDtl billLocateRuleDtl,Category category,
			List<BillLocateRuleDtl> listDelteds,List<BillLocateRuleDtl> listInserteds) throws ManagerException {
		try{
			
			List<Category> listCategorys = new ArrayList<Category>();
			if(StringUtils.isNotBlank(billLocateRuleDtl.getRuleNo())){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", billLocateRuleDtl.getLocno());
				params.put("ruleNo", billLocateRuleDtl.getRuleNo());
				List<BillLocateRuleDtl> listDelDtls = billLocateRuleDtlService.findBillLocateRuleDtlFilter(params, listDelteds);
			
				if(CommonUtil.hasValue(listDelDtls)){
					for (BillLocateRuleDtl dtl : listDelDtls) {
						Category c = new Category();
						c.setCateNo(dtl.getRuleCateno());
						listCategorys.add(c);
					}
				}
			}
			
			if(CommonUtil.hasValue(listInserteds)){
				for (BillLocateRuleDtl dtl : listInserteds) {
					Category c = new Category();
					c.setCateNo(dtl.getRuleCateno());
					listCategorys.add(c);
				}
			}
			
			Map<String,Object> categoryParams = new HashMap<String, Object>();
			categoryParams.put("cateNo", category.getCateNo());
			categoryParams.put("cateName", category.getCateName());
			return billLocateRuleDtlService.findCategoryFilterCount(categoryParams, listCategorys);
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<Category> findCategoryFilterByPage(SimplePage page,
			BillLocateRuleDtl billLocateRuleDtl,Category category,
			List<BillLocateRuleDtl> listDelteds,List<BillLocateRuleDtl> listInserteds) 
			throws ManagerException {
		try{
			
			List<Category> listCategorys = new ArrayList<Category>();
			//if(StringUtils.isNotBlank(billLocateRuleDtl.getRuleNo())){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", billLocateRuleDtl.getLocno());
				//params.put("ruleNo", billLocateRuleDtl.getRuleNo());
				List<BillLocateRuleDtl> listDelDtls = billLocateRuleDtlService.findBillLocateRuleDtlFilter(params, listDelteds);
				
				if(CommonUtil.hasValue(listDelDtls)){
					for (BillLocateRuleDtl dtl : listDelDtls) {
						Category c = new Category();
						c.setCateNo(dtl.getRuleCateno());
						listCategorys.add(c);
					}
				}
			//}
			
			if(CommonUtil.hasValue(listInserteds)){
				for (BillLocateRuleDtl dtl : listInserteds) {
					Category c = new Category();
					c.setCateNo(dtl.getRuleCateno());
					listCategorys.add(c);
				}
			}
			
			Map<String,Object> categoryParams = new HashMap<String, Object>();
			categoryParams.put("cateNo", category.getCateNo());
			categoryParams.put("cateName", category.getCateName());
			return billLocateRuleDtlService.findCategoryFilterByPage(page, categoryParams, listCategorys);
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		
	}
}