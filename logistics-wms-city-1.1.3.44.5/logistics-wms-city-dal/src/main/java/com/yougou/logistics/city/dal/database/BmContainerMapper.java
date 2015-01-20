package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BmContainer;
/**
 * 容器基本资料
 * @author wanghb
 * @date   2014-7-30
 * @version 1.1.3.36
 */
public interface BmContainerMapper extends BaseCrudMapper {
	
	int batchUpdate(List<BmContainer> bmContainer)throws DaoException;
	
	int batchStatusByBillConAdj(@Param("params")Map<String,Object> map)throws DaoException;
	
	int batchConBoxStatusByBillConAdj(@Param("params")Map<String,Object> map)throws DaoException;
	
}