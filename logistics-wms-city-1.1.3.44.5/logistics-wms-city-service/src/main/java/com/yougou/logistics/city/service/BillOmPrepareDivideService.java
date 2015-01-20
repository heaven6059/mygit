package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
public interface BillOmPrepareDivideService extends BaseCrudService {
	/**
	 * 保存主表信息
	 * 
	 * @param receipt
	 * @param insertDetail
	 * @throws ServiceException
	 */
	public void saveMain(BillImReceipt receipt) throws ServiceException;

	/**
	 * 修改
	 * 
	 * @param receipt
	 * @param detailMap
	 * @return TODO
	 */
	public Map<String, Object> update(BillImReceipt receipt, List<BillImReceiptDtl> insert,List<BillImReceiptDtl> del) throws ServiceException;

	// 删除明细
	public void delDtal(BillImReceipt receipt, BillImReceiptDtl dtl) throws ServiceException;

	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 确认收货
	 * 
	 * @param receipt
	 */
	public int auditReceipt(BillImReceipt receipt) throws ServiceException;

	public int findBoxNo4DivideCount(Map map,AuthorityParams authorityParams) throws ServiceException;

	public List<?> findBoxNo4Divide(SimplePage page, @Param("params") Map map,AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 关闭预分货单
	 * @param receiptList
	 * @throws ServiceException
	 */
	public void overPrepareDivide(List<BillImReceipt> receiptList,String userName) throws ServiceException;
}