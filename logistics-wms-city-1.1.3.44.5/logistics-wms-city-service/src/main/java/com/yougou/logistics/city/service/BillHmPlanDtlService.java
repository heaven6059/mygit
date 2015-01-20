package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

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
public interface BillHmPlanDtlService extends BaseCrudService {

	/**
	 * 查询最大的行号
	 * @param billHmPlanDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillHmPlanDtl billHmPlanDtl) throws ServiceException;

    /**
     * 根据退厂计划转退厂生成明细
     * @param params
     * @return
     * @throws ServiceException
     */
    public int addByWmPlan(List<BillHmPlanDtl> addPlanDtlList) throws ServiceException;
    
	/**
	 * 重复记录检查
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillHmPlanDtl> findDuplicateRecord(Map<String, Object> params) throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	public List<BillHmPlanDtl> findInsertHmPlan4WmPlan(Map<String, Object> params) throws ServiceException;
	
	public List<BillHmPlanDtl> selectBillHmPlanDtl (Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
}