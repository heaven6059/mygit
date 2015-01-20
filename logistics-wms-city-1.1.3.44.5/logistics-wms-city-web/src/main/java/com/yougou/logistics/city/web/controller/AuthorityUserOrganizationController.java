package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.AuthorityUserOrganizationDto;
import com.yougou.logistics.city.common.model.AuthorityUserOrganization;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.AuthorityUserOrganizationManager;

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
@Controller
@RequestMapping("/user_organization")
public class AuthorityUserOrganizationController extends BaseCrudController<AuthorityUserOrganization> {
	@Log
	private Logger log;
	@Resource
    private AuthorityUserOrganizationManager authorityUserOrganizationManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityUserOrganization/",authorityUserOrganizationManager);
    }
    
    @RequestMapping(value = "/findUserOrganization")
    @ResponseBody
	public Map<String, Object> findUserOrganization(HttpServletRequest req)throws ManagerException {
    	Map<String, Object> map = new HashMap<String, Object>();
		List<AuthorityUserOrganizationDto> list = null;
		try {
			Object u = req.getSession().getAttribute(PublicContains.SESSION_USER);
			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			SystemUser user;
			if(u != null && systemId != null && areaSystemId != null){
				user = (SystemUser)u;
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("userId", user.getUserid());
				params.put("organizationType", 22);
				params.put("systemId", systemId);
				params.put("areaSystemId", areaSystemId);
				params.put("status", 1);
				list = authorityUserOrganizationManager.findUserOrganizationByParams(params);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(list == null){
			list = new ArrayList<AuthorityUserOrganizationDto>();
		}
		map.put("list", list);
		return map;
	}
}