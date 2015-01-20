package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 17:59:52 CST 2014
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
public interface BillWmRequestDtlService extends BaseCrudService {
    public SumUtilMap selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
    
    /**
     * 根据退厂计划转退厂申请生成明细
     * @param params
     * @return
     * @throws ServiceException
     */
    public int addByWmPlan(Map<String,Object> params) throws ServiceException; 
}