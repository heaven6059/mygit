package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货复核单mapper
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:19:14
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmRecheckMapper extends BaseCrudMapper {
	
	/**
	 * 审核拣货复核单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procOmOutStockRecheckAudit(Map<String, String> map) throws DaoException;
	
	/**
	 * 删除复核单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procDeleteBillOmRecheck(Map<String, String> map) throws DaoException;
	
	/**
	 * 删除拣货复核单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procOmOutStockRecheckDel(Map<String, String> map) throws DaoException;
	
	/**
	 * 复核单完成调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procRecheckComplete(Map<String, String> map) throws DaoException;
	
	/**
	 * 复核审核调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procOmRecheckAudit(Map<String, String> map) throws DaoException;
	
	/**
	 * 验证是否存在有差异的复核数量(判断分货单是否更改手工关单状态)
	 * @param billOmRecheck
	 * @return
	 * @throws DaoException
	 */
	public int checkDiffRecheckQty(BillOmRecheck billOmRecheck) throws DaoException;
	
	/**
	 * 取分货区的储位
	 * @return
	 * @throws DaoException
	 */
	public String selectOwnerCellNo(BillOmRecheck billOmRecheck) throws DaoException;
	
	public SumUtilMap<String, Object> selectRecheckSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public SumUtilMap<String, Object> selectOutstockRecheckSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	
	/**
	 * 复核打包总页数
	 * @param billOmLocate
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectOutstockRecheckCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 复核打包信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billOmExp
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheck> selectOutstockRecheckByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	public List<BillOmRecheck> selectRecheckByStoreAndCheckNo(@Param("params") Map<String, Object> map,@Param("list")List<Store> storeList)throws DaoException;
	/**
	 * 查询复核单据中箱容器以及数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheck> findBillOmRecheckConNo(@Param("params") Map<String, String> map)throws DaoException;
	
	/**
	 * 查询来源单号总数量
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectCount4Source(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 分页查询来源单号
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheck> selectByPage4Source(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
}