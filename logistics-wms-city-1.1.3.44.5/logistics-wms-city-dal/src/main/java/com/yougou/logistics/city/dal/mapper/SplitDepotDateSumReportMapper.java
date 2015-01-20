package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午12:25:48
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface SplitDepotDateSumReportMapper extends BaseCrudMapper {
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<SplitDepotDateSumReport> selectAllConContentHistory(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public int selectConContentHistoryCountByDate(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<SplitDepotDateSumReport> selectBillDetailByDate(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<SplitDepotDateSumReport> selectConContentHistoryByDate(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
}
