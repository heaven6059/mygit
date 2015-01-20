/*
 * 类名 com.yougou.logistics.city.dal.mapper.BmPrintLogMapper
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
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
import com.yougou.logistics.city.common.model.BmPrintLog;

public interface BmPrintLogMapper extends BaseCrudMapper {

    public List<String> selectLabelNo(@Param("param") Map<String, String> map)
	    throws DaoException;

    public int selectSumQty(@Param("params") Map<String, Object> map);

    public void clearLabelNo(@Param("param") Map<String, String> map)
	    throws DaoException;

    public void batchInsert(List<BmPrintLog> list) throws DaoException;

    public int selectMaxRowId(@Param("param") BmPrintLog log)
	    throws DaoException;
}