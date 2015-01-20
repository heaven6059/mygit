package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillOmDivideDtlMapper extends BaseCrudMapper {

	/**
	 * 更新分货人员
	 * @param divideDtl
	 * @return
	 */
	public int updateBillOmDivideByDivideNoAndlocno(BillOmDivideDtl divideDtl);

	/**
	 * 查询是否存在未完成分货的明细
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectBillOmDivideDtlIsComplete(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 查询分货明细带PO单号
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectBillOmDivideDtlAndPoNo(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 查询客户分组
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectBillOmDivideDtlStoreGroupBy(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 查询分货单根据SourceNo
	 * @param divideDtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectBillOmDivideDtlBySourceNo(BillOmDivideDtl divideDtl) throws DaoException;

	/**
	 * 查询客户流道编码
	 * @param divideDtl
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivideDtl selectDivideDtlSerialNo(BillOmDivideDtl divideDtl) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;

	public List<BillOmDivideDtl> selectAllDetail4Print(BillOmDivide divide) throws DaoException;

	public List<String> selectAllSourceNo(BillOmDivide divide) throws DaoException;
	
	/**
	 * 查询分货单明细
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectDivideDtl4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
	
	public void deleteDivideDtl4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
	
	public int selectDivideDtlItemQtySum(@Param("params") Map<String, Object> params) throws DaoException;
	
	/**
	 * 查询分货明细未复核的箱号
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDivideDtl> selectDivideDtlNotRecheckBox(@Param("params") Map<String, Object> params) throws DaoException;
	
	public List<BillOmDivideDtl> selectDivideDtl4Different(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 差异单删除回写分货单明细状态
	 * @param divideDtl
	 * @return
	 * @throws DaoException
	 */
	public int updateDivideDtl4Different(BillOmDivideDtl divideDtl) throws DaoException;
	
	/**
	 * 根据差异调整单查询分货明细数据
	 * @param divideDtl
	 * @return
	 * @throws DaoException
	 */
	public BillOmDivideDtl selectDivideDtlByDifferent(BillOmDivideDtl divideDtl) throws DaoException;
	
	/**
	 * 获取DIVIDEID
	 * @return
	 * @throws DaoException
	 */
	public Long selectDivideId() throws DaoException;
}