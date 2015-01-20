package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BmContainerLog;
/**
 * 容器操作日志
 * @author wanghb
 * @date   2014-8-16
 * @version 1.1.3.39
 */
public interface BmContainerLogMapper extends BaseCrudMapper {
	
	Integer findDtlCount(Map<String, Object> params)  throws DaoException;
	
	List<BmContainerLog>  findDtlByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException ;

}