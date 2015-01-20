package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.dal.database.AuthorityUserinfoMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Jun 30 17:31:36 CST 2014
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
@Service("authorityUserinfoService")
class AuthorityUserinfoServiceImpl extends BaseCrudServiceImpl implements AuthorityUserinfoService {
    @Resource
    private AuthorityUserinfoMapper authorityUserinfoMapper;

    @Override
    public BaseCrudMapper init() {
        return authorityUserinfoMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void insertBatchUserInfo(List<AuthorityUserinfo> list) throws ServiceException {
		try {
			authorityUserinfoMapper.insertBatchUserInfo(list);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AuthorityUserinfo> selectAuthorityUser4UserCenter() throws ServiceException {
		try {
			return authorityUserinfoMapper.selectAuthorityUser4UserCenter();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteAllAuthorityUserinfo() throws ServiceException {
		try {
			authorityUserinfoMapper.deleteAllAuthorityUserinfo();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public AuthorityUserinfo findUserByCache(String locno,String loginName,Map<String, AuthorityUserinfo> cache){
		AuthorityUserinfo user = null;
		String key = locno+"_"+loginName;
		if(cache != null){
			user = cache.get(key);
			if(user != null){
				return user;
			}
		}
		Map<String,Object> params = new HashMap<String, Object>();
		//params.put("locno", locno);
		params.put("loginName", loginName);
		List<AuthorityUserinfo> list = authorityUserinfoMapper.selectByParams(null, params);
		if(list != null && list.size() > 0){
			user = list.get(0);
			if(cache != null){
				cache.put(key, user);
			}
			return user;
		}
		return null;
	}
	@Override
	public AuthorityUserinfo findUserByLoginName(String locno,String loginName,int systemId,int areaSystemId)
			throws ServiceException{
		AuthorityUserinfo user = null;
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("loginName", loginName);
		params.put("systemId", systemId);
		params.put("areaSystemId", areaSystemId);
		try {
			user = authorityUserinfoMapper.selectUserByLoginName(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return user;
	}
}