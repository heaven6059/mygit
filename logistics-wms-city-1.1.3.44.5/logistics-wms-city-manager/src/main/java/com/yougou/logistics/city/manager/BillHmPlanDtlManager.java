package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
public interface BillHmPlanDtlManager extends BaseCrudManager {
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 检查明细储位的有效性
	 * @param planNo
	 * @param locoNo
	 * @param cellNo
	 * @return
	 * @throws ManagerException
	 */
	public Map<String,Object> checkDtlCell(String planNo, String locoNo, String cellNo) throws ManagerException;
	
	
	public Map<String,Object> importStorelockDtlExcel(List<BillHmPlanDtl> list,AuthorityParams authorityParams ,Map<String, Object> params)throws ManagerException;
	
}