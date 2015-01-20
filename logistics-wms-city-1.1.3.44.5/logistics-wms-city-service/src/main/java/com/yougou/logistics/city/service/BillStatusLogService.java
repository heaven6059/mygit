package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.api.dto.BillStatusLogDto;
import com.yougou.logistics.city.common.model.BillStatusLog;

public interface BillStatusLogService extends BaseCrudService {
	/**
	 * 根据预到货通知单分销合同号(s_po_no)获取状态日志信息
	 * @param spoNo
	 * @return
	 * @throws ServiceException
	 */
	public List<BillStatusLogDto> findBillStatusLogByPoNo(String poNo) throws ServiceException;
	/**
	 * 根据发货通知单来源单号(sourceexp_no)获取状态日志信息
	 * @param sourceExpNo
	 * @return
	 * @throws ServiceException
	 */
	public List<BillStatusLogDto> findBillStatusLogBySourceExpNo(String sourceExpNo) throws ServiceException;

	/**
	 * 插入状态记录
	 * @param locno 仓别
	 * @param billNo 单据号
	 * @param billType 单据类型    IM ：收货   OM：分货   UM：退仓
	 * @param status 单据对应的状态
	 * @param description 操作内容
	 * @param operator 操作人
	 * @throws ServiceException
	 */
	public void procInsertBillStatusLog(String locno, String billNo, String billType, String status,
			String description, String operator) throws ServiceException;

	/**
	 * 关联预到货通知单查询状态日志信息数量
	 * @param params
	 * @param authorityParams TODO
	 * @param dataAccessRuleEnum TODO
	 * @return
	 * @throws ServiceException
	 */
	public int findCountByIm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
	/**
	 * 关联预到货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @param authorityParams TODO
	 * @param dataAccessRuleEnum TODO
	 * @return
	 * @throws ServiceException
	 */
	public  List<BillStatusLog> findPageByIm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
	/**
	 * 关联发货通知单查询状态日志信息数量
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findCountByOm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
	/**
	 * 关联发货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public  List<BillStatusLog> findPageByOm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
	/**
	 * 关联店退仓单查询状态日志信息数量
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findCountByUm(Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
	/**
	 * 关联店退仓单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public  List<BillStatusLog> findPageByUm(SimplePage page,Map<String,Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum)throws ServiceException;
}