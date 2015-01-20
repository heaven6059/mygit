package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillStatusLog;

public interface BillStatusLogManager extends BaseCrudManager {
	
	/**
	 * 插入状态记录
	 * @param locno 仓别
	 * @param billNo 单据号
	 * @param billType 单据类型    IM ：收货   OM：分货   UM：退仓
	 * @param status 单据对应的状态
	 * @param description 操作内容
	 * @param operator 操作人
	 * @throws ManagerException
	 */
	public void procInsertBillStatusLog(String locno, String billNo, String billType, String status,
			String description, String operator) throws ManagerException;
	/**
	 * 关联预到货通知单查询状态日志信息数量
	 * @param params
	 * @param authorityParams TODO
	 * @param dataAccessRuleEnum TODO
	 * @return
	 * @throws ManagerException
	 */
	public int findCountByIm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
	/**
	 * 关联预到货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @param authorityParams TODO
	 * @param dataAccessRuleEnum TODO
	 * @return
	 * @throws ManagerException
	 */
	public  List<BillStatusLog> findPageByIm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
	/**
	 * 关联发货通知单查询状态日志信息数量
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int findCountByOm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
	/**
	 * 关联发货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public  List<BillStatusLog> findPageByOm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
	/**
	 * 关联店退仓单查询状态日志信息数量
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int findCountByUm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
	/**
	 * 关联店退仓单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public  List<BillStatusLog> findPageByUm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ManagerException;
}