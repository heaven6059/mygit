package com.yougou.logistics.city.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.common.annotation.IgnoredInterceptors;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.SystemSettingManager;
import com.yougou.logistics.city.web.interceptor.CookieUtils;

@Controller
public class MainController {

	@Autowired
	private SystemSettingManager systemSettingManager;

	@Log
	private Logger log;

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest req, Model model) {
		//		SystemUser user=new SystemUser();
		//		user.setUserid(111L);
		//		user.setUsername("管理员");
		//		user.setLoginName("admin");
		//		req.getSession().setAttribute(PublicContains.session_user, user);

		return "index";
	}

	@RequestMapping(value = "/index2")
	public String index2(HttpServletRequest req, Model model) {
		return "index2";
	}

	@RequestMapping(value = "/center")
	public String center(HttpServletRequest req, Model model) {

		return "center";
	}

	@RequestMapping(value = "/left")
	public String left(HttpServletRequest req, Model model) {

		return "left";
	}

	@RequestMapping(value = "/left2")
	public String left2(HttpServletRequest req, Model model) {

		return "left2";
	}

	//@IgnoredInterceptors
	@RequestMapping(value = "/login")
	public String login(String loginName, String loginPassword, String locNo, String locname, String flag,
			String cookieFlag, HttpServletRequest req, HttpServletResponse response, Model model) {

		//        log.info(req.getScheme());
		//        log.info(req.getServerName());
		//        log.info(req.getServerPort()+"");
		//        log.info(req.getContextPath());
		//		
		//        log.info(req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/login");
		//		

		if (!CommonUtil.hasValue(flag)) {
			model.addAttribute("error", "");
			return "login";
		}

		//1.判断用户名不能为空
		if (!CommonUtil.hasValue(loginName) || !CommonUtil.hasValue(loginPassword) || !CommonUtil.hasValue(locNo)) {
			logoutMethod(req, response);
			model.addAttribute("error", "用户名或密码为空或仓别为空!");
			return "login";
		}

		//2.如果用户已登录  则直接跳过登录过程
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		if (user != null) {
			//	    	if(loginName.equals(user.getLoginName())){
			//	    		return "forward:index";
			//	    	}else{
			//	    		logoutMethod(req,response);
			//	    	}
			logoutMethod(req, response);
		}

		//3.验证用户名和密码
		SystemUser loginUser = new SystemUser();
		String passWord = CommonUtil.md5(loginPassword); //md5加密  
		loginUser.setLoginName(loginName);
		loginUser.setLoginPassword(passWord);
		List<SystemUser> systemUserList = this.systemSettingManager.querySystemUserList(loginUser);
		//登录成功
		if (CommonUtil.hasValue(systemUserList)) {
			loginUser = systemUserList.get(0);
			loginUser.setLocNo(locNo);
			loginUser.setLocName(locname);
			req.getSession().setAttribute(PublicContains.SESSION_USER, loginUser);

			//设置cookie保存登录信息
			if (CommonUtil.hasValue(cookieFlag) && cookieFlag.equals("1")) {
				CookieUtils.addCookie(req, response, PublicContains.LOGIN_SYSTEM_USER_COOKIE_ID,
						loginUser.getLoginName(), PublicContains.LOGIN_COOKIE_TIME);
			}

		} else {
			model.addAttribute("error", "用户名或密码错误!");
			return "login";
		}

		return "forward:index";
	}

	/**
	 * 注销
	 * @throws Exception 
	 */
	@RequestMapping("/logout")
	public String logout(String loginName, String loginPassword, String flag, HttpServletRequest req,
			HttpServletResponse response, Model model) {
		logoutMethod(req, response);
		return "login";

	}

	private void logoutMethod(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			//1.清除登录用户session
			session.removeAttribute(PublicContains.SESSION_USER);

			//2.删除资源、菜单、权限等。。。。

		}
	}

	@IgnoredInterceptors
	@RequestMapping(value = "/toLogin")
	public String toLogin(HttpServletRequest req, Model model) {
		return "login";
	}

}
