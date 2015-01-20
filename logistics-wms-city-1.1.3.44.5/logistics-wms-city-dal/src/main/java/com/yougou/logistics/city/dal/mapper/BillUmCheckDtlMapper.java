/*
 * 类名 com.yougou.logistics.city.dal.database.BillUmCheckDtlMapper
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmCheckDtlMapper extends BaseCrudMapper {

	/**
	 * 退仓验收单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillUmCheckDtl> selectBillUmCheckDtlByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 退仓验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectBillUmCheckDtlCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询商品是否存在
	 * @param billUmCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectItemNoIsHave(BillUmCheckDtl billUmCheckDtl) throws DaoException;

	/**
	 * 查询明细最大的行号
	 * @param billUmCheck
	 * @return
	 * @throws DaoException
	 */
	public int selectMaxRowId(@Param("params") BillUmCheck billUmCheck) throws DaoException;

	/**
	 * 
	 * @param untread
	 * @throws DaoException
	 */
	public void deleteAllDetail(@Param("params") BillUmCheck check) throws DaoException;

	public int selectItemCount4Check(@Param("params") Item item,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<Item> selectItem4Check(@Param("params") Item item, @Param("page") SimplePage page,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int selectCountForDiff(@Param("params") Map map) throws DaoException;

	public List<BillUmCheckDtl> selectByPageForDiff(@Param("page") SimplePage page, @Param("params") Map map)
			throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public SumUtilMap<String, Object> selectPageSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 统计总数
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public int getCount(@Param("params") BillCheckImRep model, @Param("authorityParams") AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckByGroup(@Param("params") BillCheckImRep model,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtl(@Param("params") BillCheckImRep model,@Param("authorityParams") AuthorityParams authorityParams);
	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillUmCheckDtl> selectCheckDtlByCheckNo(@Param("params") Map<String, Object> params) throws DaoException;
	public void updateConvertQty4Convert(@Param("params") Map<String, Object> params,@Param("list")List<BillUmCheck> list) throws DaoException;
	public int selectCheckIsDiff(@Param("params") Map<String, Object> params)throws DaoException;
	public int selectValidateUmCheckIsRecheck(@Param("params") Map<String, Object> params) throws DaoException;
	public int selectCheckDtlRecheckQtySum(@Param("params") Map<String, Object> params) throws DaoException;
	public List<BillUmCheckDtl> selectCheckQtyJoinContent(@Param("params") Map<String, Object> params,@Param("list")List<BillUmCheck> list) throws DaoException;
	public void updateRecheckQty4Convert(@Param("params") Map<String, Object> params) throws DaoException;
}