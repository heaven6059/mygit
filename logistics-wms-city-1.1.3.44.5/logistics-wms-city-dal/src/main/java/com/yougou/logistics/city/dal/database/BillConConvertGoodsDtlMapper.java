/*
 * 类名 com.yougou.logistics.city.dal.database.BillConConvertGoodsDtlMapper
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillConConvertGoodsDtlMapper extends BaseCrudMapper {

	/**
	 * 查询最大的ID
	 * @param check
	 * @return
	 * @throws DaoException
	 */
	public int selectMaxRowId(BillConConvertGoods convertGoods) throws DaoException;

	/**
	 * 查询来源储位
	 * @return
	 * @throws DaoException
	 */
	public void selectSCellNo(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 查询目的储位
	 * @return
	 * @throws DaoException
	 */
	public String selectDCellNo(@Param("params") Map<String, Object> params) throws DaoException;
	
	
	/**
	 * 查询属性转换目的储位
	 * @return 获取的是退货暂存区某商类型的区域
	 * @throws DaoException
	 */
	public String selectDCellNotoPropetyChange(@Param("params") Map<String, Object> params) throws DaoException;

	/**
	 * 批量插入明细
	 * @param list
	 * @throws DaoException
	 */
	public void insertBatchDtl(List<BillConConvertGoodsDtl> list) throws DaoException;

	/**
	 * 库存转货明细单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillConConvertGoodsDtl> selectConvertGoodsDtlGroupByCheckByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 库存转货明细汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectConvertGoodsDtlGroupByCheckCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询所有的验收单groupby
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillConConvertGoodsDtl> selectConvertGoodsDtlGroupByCheck(@Param("params") Map<String, Object> params)
			throws DaoException;

	/**
	 * 验收单明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectGroupByCheckSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 明细合计汇总
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	
	public List<BillConConvertGoodsDtl> selectItemDtlByParams(@Param("params") Map<String, Object> params)
			throws DaoException;
	
	public List<BillConConvertGoodsDtl> selectItemContentDtlByParams(@Param("params") Map<String, Object> params)
			throws DaoException;
	
	public List<BillConConvertGoodsDtl> selectCheckContent4Convert (@Param("params") Map<String, Object> params)
			throws DaoException;
	

	/**
	 * 明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConConvertGoodsDtlSizeDto> selectDtl4SizeHorizontal(@Param("params")Map<String, Object> params, 
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;

	/**
	 * 更新箱状态为3,容器状态
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateBoxStatus4Container(@Param("params") Map<String, Object> params)throws DaoException;
}