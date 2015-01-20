package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillConConvertDtl;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
public interface BillConConvertDtlManager extends BaseCrudManager {
	
	public int findContentCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public List<BillConConvertDtl> findContentByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 保存明细
	 * @param locno
	 * @param convertNo
	 * @param ownerNo
	 * @param operator
	 * @param destLocno
	 * @param insertList
	 * @param deleteList
	 * @param updatedList
	 * @return
	 * @throws ManagerException
	 */
	public String saveDtl(String locno,String convertNo,String ownerNo,String operator,String destLocno,String creatorName,String editorName,List<BillConConvertDtl> insertList,List<BillConConvertDtl> deleteList,List<BillConConvertDtl> updatedList)throws ManagerException;
	public Map<String, Object> findSumQty(Map<String, Object> params);
	/**
	 * 明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public List<Map<String, Object>> findDtl4SizeHorizontal(String keys, AuthorityParams authorityParams)throws ManagerException;
}