package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 装车单详情mapper
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:23:31
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverDtlMapper extends BaseCrudMapper {
	 
	public int selectMaxNum(BillOmDeliverDtlKey keyObj);
	
	public int selectDeliverDtl(BillOmDeliverDtlKey keyObj);
	
	public int deleteByPrimaryKey(BillOmDeliverDtlKey keyObj);
	
	

	
	 /**
	  * 查询复核单箱信息
	  * @param params
	  * @param authorityParams
	  * @return
	  */
	 public int boxSelectCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> boxSelectQuery(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> selectRecheckDetail(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> boxDtlByParams(@Param("model")BillOmDeliverDtl modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public int selectBoxCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> selectBoxStore(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 /**
	  * 查询派车单箱信息
	  * @param params
	  * @param authorityParams
	  * @return
	  */
	 public int loadBoxSelectCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> loadBoxSelectQuery(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> selectLoadproposeDetail(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> flagDtlByParams(@Param("model")BillOmDeliverDtl modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public int selectFlagCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverDtl> selectFlagStore(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 
	/**
	 * 查询出库配送单的所有箱号
	 * @param billWmDeliverDtlKey
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmDeliverDtl> selectBoxNoByDetail(BillOmDeliverDtl billOmDeliverDtl)throws DaoException;
	
	/**
	 * 查询装车出库明细
	 * @param params
	 * @return
	 */
	public int selectDeliverDtlBoxCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillOmDeliverDtl> selectDeliverDtlBoxByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public int selectLoadproposeDeliverDtlBoxCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillOmDeliverDtl> selectLoadproposeDeliverDtlBoxByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillOmDeliverDtlSizeDto> selectDtl4SizeHorizontal(@Param("deliverNo") String deliverNo);
}