package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.common.vo.ResultVo;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.Store;

/**
 * 
 * TODO: 分货任务单Service接口
 * 
 * @author su.yq
 * @date 2013-10-14 下午7:52:44
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDivideService extends BaseCrudService {

	/**
	 * 创建分货任务单
	 * @param divide
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException;

	/**
	 * 删除分货任务单
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public void deleteBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException;

	/**
	 * 完结分货任务单
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public ResultVo modifyCompleteBillOmDivide(BillOmDivide divide) throws ServiceException;

	/**
	 * 汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmDivide> selectDivideCollectByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 汇总总数
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public int selectDivideCollectCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 根据产生查询dtl表的客户信息
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<Store> selectStoreByParam(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询分货单中需要复核的商品信息
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillOmDivideDtl> queryRecheckBoxItem(Map<String, Object> params) throws ServiceException;

	/**
	 * 手工关闭分货单
	 * @param billOmDivide
	 * @throws ServiceException
	 */
	public void procOmDivideOver(BillOmDivide billOmDivide) throws ServiceException;

	public List<Map<String, Object>> getBatchPrintInfo(List<BillOmDivide> listOmDivides) throws ServiceException;

}