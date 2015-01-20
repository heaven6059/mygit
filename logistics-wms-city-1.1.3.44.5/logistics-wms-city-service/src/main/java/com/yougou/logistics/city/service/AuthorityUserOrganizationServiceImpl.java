package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.AuthorityUserOrganizationDto;
import com.yougou.logistics.city.dal.database.UserOrganizationMapper;

/*
 * 请写出类的用途 
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
@Service("userOrganizationService")
class AuthorityUserOrganizationServiceImpl extends BaseCrudServiceImpl implements AuthorityUserOrganizationService {
    @Resource
    private UserOrganizationMapper userOrganizationMapper;

    @Override
    public BaseCrudMapper init() {
        return userOrganizationMapper;
    }

	@Override
	public List<AuthorityUserOrganizationDto> findUserOrganizationByParams(Map<String, Object> params)
			throws ServiceException {
		try {
			return userOrganizationMapper.selectUserOrganizationByParams(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
    
}