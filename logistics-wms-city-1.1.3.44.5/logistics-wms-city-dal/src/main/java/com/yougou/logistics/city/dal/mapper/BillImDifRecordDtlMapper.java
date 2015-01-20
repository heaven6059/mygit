package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
public interface BillImDifRecordDtlMapper extends BaseCrudMapper {
	public int selectContentCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillImDifRecordDtl> selectContent(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public int selectMaxPid(BillImDifRecordDtl modelType)throws DaoException;
}