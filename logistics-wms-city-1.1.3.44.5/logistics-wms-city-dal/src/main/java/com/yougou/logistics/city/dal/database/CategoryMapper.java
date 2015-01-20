package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.model.Category;

public interface CategoryMapper extends BaseCrudMapper {
	
	public abstract Category selectByCode(String code) throws DaoException;
	
	public List<CategorySimpleDto> selectCategory4Simple(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
}