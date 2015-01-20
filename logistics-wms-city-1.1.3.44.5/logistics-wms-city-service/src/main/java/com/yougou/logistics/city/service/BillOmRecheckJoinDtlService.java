package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto2;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货交接单明细service
 * 
 * @author luo.hl
 * @date 2013-10-11 上午11:21:52
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmRecheckJoinDtlService extends BaseCrudService {
	/**
	 * 查询单号
	 * @param page TODO
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public List<?> findRecheckNo(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询单号数量
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findRecheckNoCount(Map<?, ?> params,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询未交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findNoReCheckCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询未交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<?> findNoReCheck(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询已交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findReCheckedCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 查询已交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckJoinDto> findReChecked(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 尺码横排，查询商品信息
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckJoinDto2> findItemDetail(BillOmRecheckJoinDto dtl) throws ServiceException;

	/**
	 * 交接确认
	 * @param rowIdstr
	 * @param locno TODO
	 * @param recheckList TODO
	 * @return 
	 * @throws ServiceException
	 */
	public Map<String, Object> sendReCheck(String rowIdstr, String locno,String user, List<BillOmRecheck> recheckList) throws ServiceException;
	
	/**
	 * 交接确认
	 * @param rowIdstr
	 * @param locno
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public void queryReCheck(Map<String, String> paramMap2) throws ServiceException;
	
	/**
	 * 根据箱号查询单号
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public BillOmRecheckDtl selectReCheckNoByLabelNo(Map<?, ?> map) throws ServiceException;
	
	public SumUtilMap<String, Object> selectNoReCheckSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	
	public SumUtilMap<String, Object> selectNoReCheckedSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
	
}