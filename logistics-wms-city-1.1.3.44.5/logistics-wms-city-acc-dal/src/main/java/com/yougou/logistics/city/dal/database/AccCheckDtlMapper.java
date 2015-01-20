package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.vo.AccCheckDtlVo;

public interface AccCheckDtlMapper extends BaseCrudMapper {

	/**
	 * 按验收单号查询商品明细
	 * @param list
	 * @throws DaoException
	 */
	public List<AccCheckDtlVo> selectAccCheckDtlByParams(@Param("params")Map<String,Object> params) throws DaoException;
}
