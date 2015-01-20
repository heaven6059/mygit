package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.Item;

public interface ItemMapper extends BaseCrudMapper {

	public abstract Item selectByCode(@Param("code") String code,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int countItemAndSize(@Param("params") Item cc,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<Item> findItemAndSizePage(@Param("page") SimplePage page, @Param("params") Item cc,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public int selectChPlanItemAndSizeCount(@Param("params") Item cc, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;

	public List<Item> selectChPlanItemAndSizeByPage(@Param("page") SimplePage page, @Param("params") Item cc,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<Item> selectChPlanItemAndSize4Brand(@Param("params") Item cc) throws DaoException;
	
	public List<Item> selectChPlanItemCheck(@Param("params") Item item) throws DaoException;
	public List<Item> selectByParams2(@Param("params")Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	
}