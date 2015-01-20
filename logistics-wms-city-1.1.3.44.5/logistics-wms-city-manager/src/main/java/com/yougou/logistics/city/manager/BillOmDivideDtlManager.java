package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.utils.SumUtilMap;
public interface BillOmDivideDtlManager extends BaseCrudManager {
	
	/**
	 * 更新分货人员
	 * @param divideDtl
	 * @return
	 */
	public int modifyBillOmDivideByDivideNoAndlocno(BillOmDivide divide) throws ManagerException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;
}