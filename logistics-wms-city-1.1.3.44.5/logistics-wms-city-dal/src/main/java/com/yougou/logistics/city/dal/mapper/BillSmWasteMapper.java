package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteKey;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
public interface BillSmWasteMapper extends BaseCrudMapper {
	public int deleteDtlById(BillSmWasteKey key) throws DaoException;
	
	public List<BillSmWaste> selectByWaste(@Param("model")BillSmWaste modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);
}