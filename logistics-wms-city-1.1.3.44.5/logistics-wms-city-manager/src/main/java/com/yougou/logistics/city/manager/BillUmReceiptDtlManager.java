package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmReceipt;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillUmReceiptDtlManager extends BaseCrudManager {
	
	public String save(BillUmReceipt billUmReceipt , String boxNo, SystemUser user) throws ManagerException;
	public int findCountByBox(Map<String,Object> params)throws ManagerException;
	/**
	 * 查询明细表中的箱子
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public  List<BillUmReceiptDtl> findByPageByBox(SimplePage page,Map<String,Object> params)throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
}