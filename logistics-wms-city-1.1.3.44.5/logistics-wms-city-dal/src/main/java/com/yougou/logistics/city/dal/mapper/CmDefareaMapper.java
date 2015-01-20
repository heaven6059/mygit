/*
 * 类名 com.yougou.logistics.city.dal.mapper.CmDefareaMapper
 * @author qin.dy
 * @date  Wed Sep 25 16:42:38 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefware;

/**
 * 
 * 库区mapper
 * 
 * @author qin.dy
 * @date 2013-9-26 上午10:06:47
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface CmDefareaMapper extends BaseCrudMapper {
	
	/**
	 * 验证查询仓区是否存在库区
	 * @param listLocateRuleDtls
	 * @return
	 */
	public List<CmDefarea> selectCmDefareaIsHaveByWareNo(@Param("params")Map<String,Object> params,@Param("list")List<CmDefware> listCmDefwares) throws DaoException;

	public abstract List<?> selectByWareAndArea(@Param("params") Map<String, Object> map) throws DaoException;

	public List<CmDefarea> findByStoreroom(@Param("params")Map<String, Object> params);
}