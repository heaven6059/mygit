package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChPlanDtl;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
public interface BillChPlanDtlService extends BaseCrudService {
	public Long findMaxId()throws ServiceException;
	public int deleteByPlanNo(BillChPlanDtl billChPlanDtl)throws ServiceException;

	public int deleteByBillKey(BillChPlanDtl billChPlanDtl)throws ServiceException;
	/**
	 * 批量插入盘点计划单明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillChPlanDtl> list) throws ServiceException;
	
	/**
	 * 重复记录检查
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChPlanDtl> findDuplicateRecord(Map<String, Object> params) throws ServiceException;
	public List<Map<String,Object>> findCellNo(Map<String, Object> params) throws ServiceException;
	
	/**
	 * 批量插入全盘储位
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public Integer batchInsertPlanDtl4AllCell(Map<String, Object> params) throws ServiceException;
}