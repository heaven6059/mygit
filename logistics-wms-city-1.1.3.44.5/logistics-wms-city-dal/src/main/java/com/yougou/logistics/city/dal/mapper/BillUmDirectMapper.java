package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
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
public interface BillUmDirectMapper extends BaseCrudMapper {

    public int selectCount4Direct(@Param("params") Map map) throws DaoException;

    public List<BillUmDirect> selectByPage4Direct(
	    @Param("page") SimplePage page, @Param("params") Map map)
	    throws DaoException;

    public Integer selectMaxRowId(@Param("params") BillUmUntreadMm mm);

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map);
}