package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * TODO: 分货任务单
 * 
 * @author su.yq
 * @date 2013-10-14 下午6:08:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDivideMapper extends BaseCrudMapper {

	/**
	 * 创建分货任务单
	 * @param divide
	 * @throws DaoException
	 */
	public BillOmDivide insertBillOmDivide(BillOmDivide divide) throws DaoException;

	/**
	 * 创建分货任务单(库存分货)
	 * @param divide
	 * @throws DaoException
	 */
	public BillOmDivide insertBillOmDivideBoxCC(BillOmDivide divide) throws DaoException;

	/**
	 * 删除分货任务单
	 * @param divide
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivide deleteBillOmDivide(BillOmDivide divide) throws DaoException;

	/**
	 * 完结分货任务单
	 * @param divide
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivide updateCompleteBillOmDivide(BillOmDivide divide) throws DaoException;

	/**
	 * 分货单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmDivide> selectDivideCollectByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 分货单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectDivideCollectCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 根据参数查询客户信息
	 * @param params divideNo locno
	 * @param authorityParams TODO
	 * @return
	 */
	public List<Store> selectStoreByParam(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 查询分货单中需要复核的商品信息
	 * @param params
	 * @return
	 */
	public List<BillOmDivideDtl> queryRecheckBoxItem(@Param("params") Map<String, Object> params);

	public int selectCountExp(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams);

	public List<BillOmDivide> selectByPageExp(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 分货单手工关闭
	 * @param map
	 * @throws DaoException
	 */
	public void procOmDivideOver(Map<String, String> map) throws DaoException;

	/**
	 * 分货单明细汇总查询总数
	 * @param params
	 * @param authorityParams TODO
	 * @param authorityParams
	 * @return
	 */
	public int selectRecheckBoxItemCount(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 分货单明细汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams TODO
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmDivideDtl> selectRecheckBoxItemByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询是否存在手工关闭的波茨
	 * @param maps
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivide> selectOmDivideByExpNo(@Param("params") Map<String, Object> maps) throws DaoException;
	
}