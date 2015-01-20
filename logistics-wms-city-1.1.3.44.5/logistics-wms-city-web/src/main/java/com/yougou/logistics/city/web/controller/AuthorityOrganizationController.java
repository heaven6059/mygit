package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.AuthorityOrganizationManager;
import com.yougou.logistics.uc.common.api.dto.AuthorityRoleWithUserListDto;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2014-1-24 下午1:40:05
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping(value = "/authority_organization")
public class AuthorityOrganizationController {
	@Log
	private Logger log;
	@Resource
	private AuthorityOrganizationManager authorityOrganizationManager;
	
	
	@RequestMapping(value = "/role.json")
	@ResponseBody
	public Object findRole(HttpServletRequest req) throws ManagerException {
		List<AuthorityRoleWithUserListDto> list = null;
		try {
			Object u = req.getSession().getAttribute(PublicContains.SESSION_USER);
			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			SystemUser user;
			if(u != null && systemId != null && areaSystemId != null){
				user = (SystemUser)u;
				list = authorityOrganizationManager.findRoleListWithUserListByOrganization(user.getLocNo(), Integer.parseInt(systemId.toString()), Integer.parseInt(areaSystemId.toString()));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(list == null){
			list = new ArrayList<AuthorityRoleWithUserListDto>();
		}
		return list;
	}
}
