package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmCheckLatePrint;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-8-14 下午3:07:04
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillUmCheckLatePrintMapper extends BaseCrudMapper {

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillUmCheckLatePrint> selectItemInfoByBarcode(@Param("params") Map<String, Object> params,
			@Param("list") List<String> list) throws DaoException;
}
