package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.Brand;

public interface BrandMapper extends BaseCrudMapper {
	//public  Brand findById(String code,@Param("authorityParams") AuthorityParams authorityParams) ;
	public abstract Brand selectByCode(String code,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	//public  List<BrandDTO> selectByParams(Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);

	 public List<Brand>  selectByParams(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	 public List<Brand>  selectMyselfByParams(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
}