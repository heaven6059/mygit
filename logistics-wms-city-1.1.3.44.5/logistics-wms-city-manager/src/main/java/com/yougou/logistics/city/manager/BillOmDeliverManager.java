package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.SystemUser;
/**
 * 
 * 装车单manager
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:25:36
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverManager extends BaseCrudManager {

	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws ManagerException
	 */
	public int deleteBatch(String ids) throws ManagerException;
	
	/**
	 * 审核装车
	 * @param ids
	 * @param auditor
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> checkBoxBillOmDeliver(String ids, SystemUser user) throws ManagerException;
	
	/**
	 * 审核派车
	 * @param ids
	 * @param auditor
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> checkBillFlagOmDeliver(String ids, String auditor) throws ManagerException;
	
	 public List<BillOmDeliver> findBillOmDeliverSum(Map<String,Object> params,AuthorityParams authorityParams)
	    	    throws ManagerException;

	/**
	 * 汇总打印
	 * @param sortColumn
	 * @param sortOrder
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ManagerException
	 */
	public List<BillOmDeliver> findPrintOmDeliverList(String sortColumn,
			String sortOrder, Map<String, Object> params,
			AuthorityParams authorityParams )throws ManagerException;
}