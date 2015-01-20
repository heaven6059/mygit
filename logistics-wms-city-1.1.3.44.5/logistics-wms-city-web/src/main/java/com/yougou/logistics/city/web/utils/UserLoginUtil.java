package com.yougou.logistics.city.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-4-1 上午9:53:54
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class UserLoginUtil {
	/**
	 * 1.获取当前登录用户
	 * @param request
	 * @return
	 */
	public static SystemUser getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		return user;
	}
	
	/**
	 * 获取权限验证参数
	 * @param request
	 * @return
	 */
	public static AuthorityParams getAuthorityParams(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		AuthorityParams authorityParams=new AuthorityParams();
		//获取用户
		SystemUser user=(SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		//获取系统关联ID
		String systemId=(String) session.getAttribute(PublicContains.SESSION_SYSTEMID);
		//获取区域系统编号
		String areaSystemId=(String) session.getAttribute(PublicContains.SESSION_AREASYSTEMID);		

		authorityParams.setUserId(user.getUserid().toString());
		authorityParams.setSystemNoVerify(systemId);//系统关联ID
		authorityParams.setAreaSystemNoVerify(areaSystemId);//区域系统编号
		return authorityParams;
	}
}
