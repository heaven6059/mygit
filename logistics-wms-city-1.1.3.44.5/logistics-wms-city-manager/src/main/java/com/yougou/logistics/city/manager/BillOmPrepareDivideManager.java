package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.SystemUser;

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
public interface BillOmPrepareDivideManager extends BaseCrudManager {

	/**
	 * 保存主表
	 * 
	 * @param receipt
	 * @throws ManagerException
	 */
	public void saveMain(BillImReceipt receipt) throws ManagerException;

	/**
	 * 修改
	 * 
	 * @param receipt
	 * @param insert
	 * @param update
	 * @param del
	 * @return TODO
	 * @throws ServiceException
	 */
	public Map<String, Object> update(BillImReceipt receipt, List<BillImReceiptDtl> insert, List<BillImReceiptDtl> del) throws ManagerException;

	/**
	 * 批量审核
	 * 
	 * @param keyStr
	 * @param locno
	 * @param ownerNo
	 * @param newParam
	 *            TODO
	 * @return
	 * @throws ManagerException
	 */
	public int auditBatch(String keyStr, String locno, String ownerNo, SystemUser user) throws ManagerException;

	/**
	 * 删除
	 * 
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public int deleteBatch(String keyStr, String locno, String ownerNo) throws ManagerException;

	/**
	 * 收货单查询
	 * 
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	public int findBoxNo4DivideCount(Map map,AuthorityParams authorityParams) throws ManagerException;

	public List<?> findBoxNo4Divide(SimplePage page, Map map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 关闭预分货单
	 * @param receiptList
	 * @throws ServiceException
	 */
	public void overPrepareDivide(List<BillImReceipt> receiptList,String userName) throws ManagerException;
}