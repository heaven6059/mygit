package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillConConvertGoods;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
public interface BillConConvertGoodsManager extends BaseCrudManager {

	/**
	 * 删除主档信息
	 * @param goodsList
	 * @throws ServiceException
	 */
	public void deleteConvertGoods(List<BillConConvertGoods> goodsList) throws ManagerException;

	/**
	 * 审核单据
	 * @param goodsList
	 * @throws ManagerException
	 */
	public void auditConvertGoods(String loginName, String userName, List<BillConConvertGoods> goodsList)
			throws ManagerException;
}