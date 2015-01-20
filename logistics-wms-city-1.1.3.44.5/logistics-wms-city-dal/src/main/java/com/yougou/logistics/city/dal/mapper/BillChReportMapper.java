
package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface BillChReportMapper extends BaseCrudMapper {

	/**
	 * 总计
	 * @param params
	 * @return
	 * @throws DaoException
	 */
    public Map<String, Integer> selectSumQty(@Param("params")Map<String, Object> params,@Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}