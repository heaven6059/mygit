/*
 * 类名 com.yougou.logistics.city.dal.database.AuthorityUserOrganizationMapper
 * @author su.yq
 * @date  Mon Feb 10 14:51:59 CST 2014
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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.AuthorityUserOrganizationDto;

public interface UserOrganizationMapper extends BaseCrudMapper {
	
    public List<AuthorityUserOrganizationDto> selectUserOrganizationByParams(@Param("params")Map<String,Object> params) throws DaoException;
	
}