package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.Item;

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
public interface BillWmPlanDtlService extends BaseCrudService {
    public List<Item> selectItem(SimplePage page, Map<String, Object> map,AuthorityParams authorityParams)
	    throws ServiceException;

    public int selectItemCount(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;

    public void saveDetail(String insertList, String deleteList,
	    BillWmPlan plan) throws ServiceException;
}