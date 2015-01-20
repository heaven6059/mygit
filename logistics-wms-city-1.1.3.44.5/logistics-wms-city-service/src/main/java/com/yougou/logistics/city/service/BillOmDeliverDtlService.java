package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 装车单详情service
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:27:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverDtlService extends BaseCrudService {
	
	/**
	 * 查询明细
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillOmDeliverDtl> selectBillOmRecheckDtl(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> selectLoadproposeDetail(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 查询最大装车ID
	 * @param keyObj
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxNum(BillOmDeliverDtlKey keyObj)throws ServiceException;
	/**
	 * 检查本装车单信息对否重复
	 * @param keyObj
	 * @return
	 * @throws ServiceException
	 */
	public int selectDeliverDtl(BillOmDeliverDtlKey keyObj)throws ServiceException;
	/**
	 * 删除明细
	 * @param keyObj
	 * @return
	 * @throws ServiceException
	 */
	public int deleteByPrimaryKey(BillOmDeliverDtlKey keyObj) throws ServiceException;
	/**
	 * 查询出库配送单的所有箱号
	 * @param billWmDeliverDtlKey
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDeliverDtl> findBoxNoByDetail(BillOmDeliverDtl billOmDeliverDtl)throws ServiceException;
	/**
	 * 复核单明细查询
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int boxSelectCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> boxSelectQuery(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> boxDtlByParams(BillOmDeliverDtl modelType,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public int selectBoxCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> selectBoxStore(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 派车单明细查询
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int flagSelectCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> flagSelectQuery(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> flagDtlByParams(BillOmDeliverDtl modelType,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public int selectFlagCount(Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	public List<BillOmDeliverDtl> selectFlagStore(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 查询装车出库明细
	 * @param params
	 * @return
	 */
	public int findDeliverDtlBoxCount(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	public List<BillOmDeliverDtl> findDeliverDtlBoxByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	/**
	 * 查询派车出库明细
	 * @param params
	 * @return
	 */
	public int findLoadproposeDeliverDtlBoxCount(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	public List<BillOmDeliverDtl> findLoadproposeDeliverDtlBoxByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;

	
	public List<BillOmDeliverDtlSizeDto> findDtl4SizeHorizontal(@Param("deliverNo") String deliverNo);
}