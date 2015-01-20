/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChCheckDtlMapper
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillChCheckDtlMapper extends BaseCrudMapper {
	/**
	 * 批量更新盘点人员
	 * 
	 * @param check
	 */
	public void updateCheckWorkerAndStatusBatch(@Param("check") BillChCheck check) throws DaoException;

	/**
	 * 查询最大的rowId
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int selectMaxRowId(@Param("check") BillChCheck check) throws DaoException;

	/**
	 * 查询盘点下的所有储位
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectCellNo(@Param("check") BillChCheck check) throws DaoException;
	
	/**
	 * 查询盘点计划明细中的储位
	 * @param planNo
	 * @param locNo
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectCellNobyPlan(@Param("planNo")String planNo,@Param("locNo")String locNo) throws DaoException;
	/**
	 * 查询盘点单明细中的储位
	 * @param planNo
	 * @param locNo
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectCellNobyCheck(@Param("checkNo")String checkNo,@Param("locNo")String locNo) throws DaoException;
	/**
	 * 查询盘点单明细中所有的商品
	 * @param checkNo
	 * @param locNo
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectItemNobyCheck(@Param("checkNo")String checkNo,@Param("locNo")String locNo) throws DaoException;


	public List<BillChCheckDtl> selectItem4ChCheck(@Param("page") SimplePage page,
			@Param("params") BillChCheckDtl check, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectItem4ChCheckCount(@Param("params") BillChCheckDtl check,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	
	public int selectCountSingFlag(@Param("params") Map<String, Object> map) throws DaoException;

	/**
	 * 查找盘点计划单内的商品
	 * @param page
	 * @param check
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectItemByPlan(@Param("page") SimplePage page,
			@Param("params") BillChCheckDtl check, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	/**
	 * 查找盘点计划单内的商品总数
	 * @param check
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectItemByPlanCount(@Param("params") BillChCheckDtl check,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	/**
	 * 批量更新状态
	 * 
	 * @param check
	 * @throws DaoException
	 */
	public void updateCheckStautsBatch(@Param("check") BillChCheck check) throws DaoException;

	/**
	 * 复盘发单明细统计信息
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectReChCheckCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillChCheckDto> selectReChCheck(@Param("params") Map<String, Object> map,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 根据盘点计划单查询盘点明细数量
	 * 
	 * @param params
	 * @return
	 */
	public int selectCountByPlanNo(@Param("params") Map<String, Object> params);

	/**
	 * 根据盘点计划单查询盘点明细
	 * 
	 * @param page
	 * @param params
	 * @return
	 */
	public List<BillChCheckDtl> selectByPageByPlanNo(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params);

	/**
	 * 查询商品明细总数（商品盘、限制品牌）
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int selectBrandLimitItemCount(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 查询商品明细（商品盘、限制品牌）
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectBrandLimitItem(@Param("page") SimplePage page,
			@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 批量插入明细
	 * 
	 * @param list
	 */
	public void batchInsertDtl(List<BillChCheckDtl> list) throws DaoException;

	public void batchUpdateDtl(List<BillChCheckDtl> list) throws DaoException;

	public List<BillChCheckDtl> selectRepeat(@Param("item") BillChCheck check) throws DaoException;

	/**
	 * 按计划保存
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int saveByPlan(@Param("item") BillChCheck check) throws DaoException;

	/**
	 * 实盘置零
	 * 
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int resetPlan(@Param("item") BillChCheck check) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty4Cell(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty4Plan(@Param("params") Map<String, Object> map);

	/**
	 * 查询存在盘点差异的盘点储位总数
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectByPage4CellCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询存在盘点差异的盘点储位
	 * @param page
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectByPage4Cell(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 获取所有信息
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectAllDetail4Print(BillChCheck check) throws DaoException;

	/**
	 * 获取明细储位总数
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int selectCellNoCount(BillChCheck check) throws DaoException;

	/**
	 * 撤销盘点单时将储位盘点状态置为可用
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int updateCellCheckStatusByCheckDtl(@Param("params") Map<String, Object> params) throws DaoException;
	/**
	 * 尺码横排
	 * @param modelType
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillChCheckDtl> selectDtlSysNo(@Param("model")BillChCheckDtl billChCheckDtl,@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;

	public int selectSysNoContentCount(@Param("model")BillChCheckDtl billChCheckDtl,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;

	public List<BillChCheckDtl> selectSysNoContentByPage(@Param("page")SimplePage page,
			@Param("orderByField") String orderByField,@Param("orderBy") String orderBy, @Param("model")BillChCheckDtl billChCheckDtl,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;

	public List<BillChCheckDtl> selectSysNoByPage(@Param("model")BillChCheckDtl c,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
}