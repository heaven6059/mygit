package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmReceipt;
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
public interface BillUmReceiptManager extends BaseCrudManager {
	/**
	 * 新增主档信息
	 * @param billUmReceipt
	 * @return
	 * @throws ManagerException
	 */
	public String addMain(BillUmReceipt billUmReceipt,SystemUser user)throws ManagerException;
	/**
	 * 删除单据(包含明细)
	 * @param deleted 仓库-收货单号-货主 
	 * @return
	 * @throws ManagerException
	 */
	public String deleteReceipt(String deleted)throws ManagerException;
	/**
	 * 审核
	 * @param billUmReceipt
	 * @return
	 * @throws ManagerException
	 */
	public String check(BillUmReceipt billUmReceipt, SystemUser user)throws ManagerException;
	/**
	 * 批量生成退仓收货单
	 * @param locnoStr
	 * @param untreadNoStr
	 * @param ownerNoStr
	 * @param user TODO
	 * @return
	 * @throws ManagerException
	 */
	public String batchCreate(String locnoStr,String untreadNoStr,String ownerNoStr, SystemUser user)throws ManagerException;
	
	public SumUtilMap<String, Object> selectUmReceiptSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
}