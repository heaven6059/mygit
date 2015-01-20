package com.yougou.logistics.city.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午3:21:23
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BaseLmp2WmsMapper extends BaseCrudMapper {

	public List<BaseLmp2WmsDto> selectBaseCheck4Wms(@Param("sysNoList")List<String> sysNoList,@Param("interFaceNameList")List<String> interFaceNameList) throws DaoException;
	
}
