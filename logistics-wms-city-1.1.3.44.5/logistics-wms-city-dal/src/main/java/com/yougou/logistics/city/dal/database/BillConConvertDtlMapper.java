/*
 * 类名 com.yougou.logistics.city.dal.database.BillConConvertDtlMapper
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillConConvertDtlSizeDto;

public interface BillConConvertDtlMapper extends BaseCrudMapper {
	
	public int selectContentCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
    
    public List<BillConConvertDtl> selectContentByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
    /**
	 * 批量插入明细
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertDtl(List<BillConConvertDtl> list) throws DaoException;
	
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params);

	/**
	 * 明细打印尺码横排
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillConConvertDtlSizeDto>  selectDetail4SizeHorizontal(@Param("params")Map<String, Object> params,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
}