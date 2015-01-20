package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.enums.BillWmPlanStatusEnums;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.dal.mapper.BillWmPlanDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillWmPlanMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
@Service("billWmPlanDtlService")
class BillWmPlanDtlServiceImpl extends BaseCrudServiceImpl implements
	BillWmPlanDtlService {

    @Resource
    private BillWmPlanDtlMapper billWmPlanDtlMapper;

    @Resource
    private BillWmPlanMapper billWmPlanMapper;
    
    @Resource
    private ItemService itemService;

    @Override
    public BaseCrudMapper init() {
	return billWmPlanDtlMapper;
    }

    @Log
    private Logger log;

    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<Item> selectItem(SimplePage page, Map<String, Object> map,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billWmPlanDtlMapper.selectItem(page, map,authorityParams);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int selectItemCount(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billWmPlanDtlMapper.selectItemCount(map,authorityParams);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public void saveDetail(String insertStrs, String deleteStrs, BillWmPlan plan)
	    throws ServiceException {
	try {
	    BillWmPlan tempPlan = billWmPlanMapper.selectByPrimaryKey(plan);
	    if (tempPlan == null
		    || !BillWmPlanStatusEnums.STATUS10.getStatus().equals(
			    tempPlan.getStatus())) {
		throw new ServiceException("只有建单状态的单据才能修改明细");
	    }
	    if (!StringUtils.isEmpty(deleteStrs)) {
		String[] items = deleteStrs.split(",");
		for (String itemNo : items) {
		    BillWmPlanDtl dtl = new BillWmPlanDtl();
		    dtl.setLocno(plan.getLocno());
		    dtl.setOwnerNo(plan.getOwnerNo());
		    dtl.setPlanNo(plan.getPlanNo());
		    dtl.setItemNo(itemNo);
		    billWmPlanDtlMapper.deleteByPrimarayKeyForModel(dtl);
		}
	    }

	    if (!StringUtils.isEmpty(insertStrs)) {
		String[] items = insertStrs.split(",");
		for (String itemNo : items) {
			Item item=itemService.selectByCode(itemNo,null);
			String brandNo =item.getBrandNo();
			
		    BillWmPlanDtl dtl = new BillWmPlanDtl();
		    dtl.setLocno(plan.getLocno());
		    dtl.setOwnerNo(plan.getOwnerNo());
		    dtl.setPlanNo(plan.getPlanNo());
		    dtl.setItemNo(itemNo);
		    dtl.setStatus(BillWmPlanStatusEnums.STATUS10.getStatus());
		    dtl.setBrandNo(brandNo);
		    billWmPlanDtlMapper.insertSelective(dtl);
		}
	    }

	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    throw new ServiceException("商品编码重复");
	}

    }
}