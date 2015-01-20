package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccContainerSku;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-08-08 13:49:01
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface AccContainerSkuMapper extends BaseCrudMapper {
	/**
	 * 查询板库存明细
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	int selectPlateCount(@Param("params")Map<String,Object> params)throws DaoException;
	
	List<AccContainerSku> selectPlateByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params)throws DaoException;
	
	List<AccContainerSku> selectPlateSub(@Param("params")Map<String,Object> params)throws DaoException;
	/**
	 * 加锁更新
	 * @param accContainerSku
	 */
	public void updateLockAccContainerSku(AccContainerSku accContainerSku);
}