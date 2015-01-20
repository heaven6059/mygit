package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.Category;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
public interface BillLocateRuleDtlManager extends BaseCrudManager {
	
	/**
	 * 查询过滤后的商品类别分页总数
	 * @param listCategorys
	 * @return
	 */
	public int findCategoryFilterCount(BillLocateRuleDtl billLocateRuleDtl,Category category,List<BillLocateRuleDtl> listDelteds,List<BillLocateRuleDtl> listInserteds) throws ManagerException;
	
	/**
	 * 查询过滤后的商品类别分页
	 * @param listCategorys
	 * @return
	 * @throws DaoException
	 */
	public List<Category> findCategoryFilterByPage(SimplePage page,BillLocateRuleDtl billLocateRuleDtl,Category category,List<BillLocateRuleDtl> listDelteds,List<BillLocateRuleDtl> listInserteds) throws ManagerException;
	
}