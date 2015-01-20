package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
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
public interface BillUmUntreadDtlManager extends BaseCrudManager {
	/**
	 * 保存明细
	 * 
	 * @param insertList
	 * @param updateList
	 * @param deleteList
	 * @param untread
	 * @throws ManagerException
	 */
	public void saveUntreadDtl(List<ConBoxDtl> insertList, List<ConBoxDtl> updateList, List<ConBoxDtl> deleteList,
			BillUmUntread untread) throws ManagerException;

	public List<BillUmUntreadDtl> selectAllBox(@Param("params") BillUmUntread untread, AuthorityParams authorityParams) throws ManagerException;

	public int findCountByBox(Map<String, Object> params) throws ManagerException;

	/**
	 * 查询明细表中的箱子
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillUmUntreadDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ManagerException;

	public int select4BoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	public List<ConBoxDtl> select4Box(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException;

	public List<Map<String, Object>> queryPrints(String locno, String keystr) throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
}