package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.Lookupdtl;

public interface LookupdtlMapper extends BaseCrudMapper {
	
	/**
	 * 查询拣货任务分派表的出库类型
	 * @param lookupdtlKey
	 * @return
	 * @throws DaoException
	 */
	public  List<Lookupdtl>  selectOutstockDirectExpType(Lookupdtl lookupDtl)throws DaoException;

	/**
	 * 根据品牌查询码表明细信息
	 * @param lookupDtl
	 * @return
	 * @throws DaoException
	 */
	public List<Lookupdtl> selectLookupdtlBySysNo(Lookupdtl lookupDtl)throws DaoException;
	
	
	public List<Lookupdtl> selectLookupdtlByCode(@Param("params")Map<String, Object> params)throws DaoException;
	
}