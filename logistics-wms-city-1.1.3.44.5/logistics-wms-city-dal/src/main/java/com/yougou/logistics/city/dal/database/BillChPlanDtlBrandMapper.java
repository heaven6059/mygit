package com.yougou.logistics.city.dal.database;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途
 * 
 * @author su.yq
 * @date 2014-11-12 14:17:19
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillChPlanDtlBrandMapper extends BaseCrudMapper {

	/**
	 * 批量插入品牌
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public Integer batchInsertPlanDtlBrand(
			@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
}