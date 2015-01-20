package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货复核单明细mapper
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:19:37
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillOmRecheckDtlMapper extends BaseCrudMapper {

	/**
	 * 查询最大ID
	 * 
	 * @param billOmRecheckDtl
	 * @return
	 */
	public int selectMaxPid(BillOmRecheckDtl billOmRecheckDtl);

	/**
	 * 查询复核明细商品的总数量
	 * 
	 * @param recheckDtl
	 * @return
	 */
	public List<BillOmRecheckDtl> selectBillOmRecheckDtlGroupBy(@Param("params") Map<String, Object> map)
			throws DaoException;

	/**
	 * 复核明细汇总分页查询
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmRecheckDtl> selectBillOmRecheckDtlByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 复核明细汇总查询总数
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectBillOmRecheckDtlCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	
	/**
	 * 复核明细汇总分页查询根据箱号汇总
	 * 
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillOmRecheckDtl> selectRecheckDtlGroupByBoxPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 复核明细汇总查询总数箱号汇总
	 * 
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectRecheckDtlGroupByBoxCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询复核明细带PO单号
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtl> selectBillOmRecheckDtlAndPoNo(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 根据箱号，商品编码分组查询
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtl> selectGroupByBoxAndItem(@Param("dtl") BillOmRecheckDtl dtl) throws DaoException;

	/**
	 * 查询分组明细
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtl> selectGroupDetail(@Param("dtl") BillOmRecheckDtl dtl) throws DaoException;

	/**
	 * 获取尺码详情
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtl> selectSizeNoDetail(@Param("dtl") BillOmRecheckDtl dtl) throws DaoException;
	
	/**
	 * 拣货复核封箱
	 * 
	 * @param map
	 * @throws DaoException
	 */
	public void procOmInsertLabel(Map<String, String> map) throws DaoException;
	
	/**
	 * 直通复核、转货复核 复核封箱
	 * 
	 * @param map
	 * @throws DaoException
	 */
	public void procDirectRecheckSealBox(Map<String, String> map) throws DaoException;

	/**
	 * 查询复核明细
	 * 
	 * @param recheckDtl
	 * @return
	 * @throws DaoException
	 */
	public BillOmRecheckDtl selectRecheckDtlByItem(BillOmRecheckDtl recheckDtl) throws DaoException;

	/**
	 * 根据复核单号和容器号获取的boxRowId
	 * 
	 * @param billOmRecheckDtl
	 * @return
	 */
	public long selectBoxRowIdByRecheckNoAndContainerNo(BillOmRecheckDtl billOmRecheckDtl);

	/**
	 * 根据复核单号获取最大的boxRowId
	 * 
	 * @param billOmRecheckDtl
	 * @return
	 */
	public long selectMaxBoxRowIdByRecheckNo(BillOmRecheckDtl billOmRecheckDtl);

	/**
	 * 查询复核明细关联外箱号
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtl> selectBillOmRecheckDtlByShowBox(@Param("params") Map<String, Object> params)
			throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public SumUtilMap<String, Object> selectRfSumQty(@Param("params") Map<String, Object> map, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 打印
	 * 
	 * @param recheck
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNo(BillOmRecheck recheck) throws DaoException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtl4Print(BillOmRecheckDtlSizeKind recheck) throws DaoException;

	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNoAndBox(BillOmRecheck recheck) throws DaoException;

	public List<BillOmRecheckDtlSizeKind> selectAllDtlBox4Print(BillOmRecheckDtlSizeKind recheck) throws DaoException;

	/**
	 * 查询所有明细
	 * @param recheck
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectAllDtlSizeKind(BillOmRecheck recheck) throws DaoException;
	
	public void deleteRecheckDtlByRealQty(@Param("params") Map<String, Object> params) throws DaoException;
	
	public BillOmRecheckDtl selectRecheckDtlIncreaseNum(@Param("params") Map<String, Object> params) throws DaoException;
	
	public BillOmRecheckDtl selectRecheckDtlSumRealQty(@Param("params") Map<String, Object> params) throws DaoException;

	public int selectCheckRecheckDtlRealQtySum(@Param("params") Map<String, Object> params) throws DaoException;
	
	public void procDirectrecheckSealBox(Map<String, String> map) throws DaoException;
	
	public int insertRecheckDtl4UmCheck(@Param("params") Map<String, Object> params) throws DaoException;
	
	public int insertRecheckDtl4SmOtherin(@Param("params") Map<String, Object> params) throws DaoException;
}