package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface ItemChDifferentReportMapper extends BaseCrudMapper{

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}
