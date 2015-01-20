package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.BillConAdjDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public interface BillConAdjDtlMapper extends BaseCrudMapper {
	
	public int selectItemCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public List<BillConAdjDtl> selectItem(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public int selectMaxRowId(BillConAdjDtl model);
	
	public int checkAty(@Param("map") Map<String,Object> map);
	
	public int checkItem(@Param("map") Map<String, Object> map);
	
	public List<BillConAdjDtl> selectContentParams(@Param("model")BillConAdjDtl modelType,@Param("params")Map<String,Object> params);

	public List<String> selectDtlCell(@Param("params")Map<String,Object> params);
	
	public List<String> selectDtlCon(@Param("params")Map<String,Object> params);
	
	public List<String> selectConIdFromDtl(@Param("params")Map<String,Object> params);

	 public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams);
	 
	 /**
		 * 批量插入明细
		 * @param list
		 * @throws DaoException
		 */
		public void batchInsertDtl(List<BillConAdjDtl> list) throws DaoException;
		
		/**
		 * 批量插入明细
		 * @param list
		 * @throws DaoException
		 */
		public void batchInsertDtl4ConvertGoods(List<BillConAdjDtl> list);
		/**
		 * 库存调整打印明细
		 */
		public List<BillConAdjDtlSizeDto> findAllDtl(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);

	public List<BillConAdjDtl> selectCheckRepeatData(@Param("params")Map<String,Object> params) throws DaoException;
	
	public List<BillConAdjDtl> selectListByConNo(@Param("params")Map<String,Object> params) throws DaoException;
	
	public int deleteByConNo(@Param("params")Map<String,Object> params)throws DaoException;

	public void updateOperateRecord(Map<String, Object> map)throws DaoException;
}