package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Nov 18 12:08:45 CST 2013
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
public interface BillUmInstockDirectManager extends BaseCrudManager {
	
	public void createTask(String locNo, String ownerNo,String sourceNo,String keyStr,String loginName) throws ManagerException;
	
	public void sendOrder(String locNo, String ownerNo,String sender,String keyStr,String loginName) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	public void akeySendOrder(BillUmInstockDirect instockDirect) throws ManagerException;

}