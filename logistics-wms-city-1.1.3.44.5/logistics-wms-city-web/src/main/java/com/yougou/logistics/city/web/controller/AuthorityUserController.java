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
import com.yougou.logistics.city.common.vo.AuthorityUserVo;
import com.yougou.logistics.city.manager.AuthorityUserManager;
import com.yougou.logistics.uc.common.api.model.AuthorityUser;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2014-2-11 上午9:37:40
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping(value = "/authority_user")
public class AuthorityUserController {
	@Log
	private Logger log;
	@Resource
	private AuthorityUserManager authorityUserManager;
	@RequestMapping(value = "/user.json")
	@ResponseBody
	public Object findRole(HttpServletRequest req)
			throws ManagerException {
		List<AuthorityUser> list = null;
		List<AuthorityUserVo> listVo = new ArrayList<AuthorityUserVo>();
		try {
			Object u = req.getSession().getAttribute(PublicContains.SESSION_USER);
			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			SystemUser user;
			if(u != null && systemId != null && areaSystemId != null){
				user = (SystemUser)u;
				list = authorityUserManager.findUserListByStoreNoAndSystemId(user.getLocNo(),  Integer.parseInt(systemId.toString()), Integer.parseInt(areaSystemId.toString()));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(list != null && list.size() > 0){
			AuthorityUserVo vo;
			for(AuthorityUser au : list){
				vo = new AuthorityUserVo();
				vo.setWorkerNo(au.getLoginName());
				vo.setWorkerName(au.getUsername());
				listVo.add(vo);
			}
		}
		return listVo;
	}
}
