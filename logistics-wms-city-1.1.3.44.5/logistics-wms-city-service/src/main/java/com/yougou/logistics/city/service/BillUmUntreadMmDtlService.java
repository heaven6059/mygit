package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
public interface BillUmUntreadMmDtlService extends BaseCrudService {
	/**
	 * 查询商品
	 * @param page
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillUmUntreadMmDtl> selectItem(SimplePage page, BillUmUntreadMmDtl dtl) throws ServiceException;

	public int selectItemCount(BillUmUntreadMmDtl dtl) throws ServiceException;

	/**
	 * 保存明细
	 * @param insertList
	 * @param updateList
	 * @param deleteList
	 * @param untread
	 * @throws ServiceException
	 */
	public void saveUntreadMmDtl(List<BillUmUntreadMmDtl> insertList, List<BillUmUntreadMmDtl> updateList,
			List<BillUmUntreadMmDtl> deleteList, BillUmUntreadMm untread) throws ServiceException;

	public List<BillUmUntreadMmDtl> selectStoreNo(BillUmUntreadMm mm) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params) throws ServiceException;
}