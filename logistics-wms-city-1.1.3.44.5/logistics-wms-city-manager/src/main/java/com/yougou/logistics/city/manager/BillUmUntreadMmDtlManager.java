package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillUmUntreadMmDtlManager extends BaseCrudManager {
	public List<BillUmUntreadMmDtl> selectItem(SimplePage page, BillUmUntreadMmDtl dtl) throws ManagerException;

	public int selectItemCount(BillUmUntreadMmDtl dtl) throws ManagerException;

	/**
	 * 保存明细
	 * @param insertList
	 * @param updateList
	 * @param deleteList
	 * @param untread
	 * @throws ManagerException
	 */
	public void saveUntreadMmDtl(List<BillUmUntreadMmDtl> insertList, List<BillUmUntreadMmDtl> updateList,
			List<BillUmUntreadMmDtl> deleteList, BillUmUntreadMm untread) throws ManagerException;

	public List<BillUmUntreadMmDtl> selectStoreNo(BillUmUntreadMm mm) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params) throws ManagerException;
}