package com.yougou.logistics.city.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.IgnoredInterceptors;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.utils.uccenter.UcCenterUtil;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.web.interceptor.CookieUtils;

/**
 * 
 * 连接用户中心控制器
 * @author zhang.zg zhangzhongguo
 * @date 2013-11-26cc
 * @copyRight yougou.com
 */
@Controller
public class UcMainController {
	@Log
	private Logger log;
	
	protected static final XLogger logger = XLoggerFactory.getXLogger(UcMainController.class);

	@Value("${sing.appkey}")
	private String appKey;

	@Value("${uc.index.url}")
	private String ucIndexUrl;
	@Value("${uc.logout.url}")
	private String ucLogoutUrl;
	@Value("${sys.version}")
	private String sysVersion;
	@Value("${isShowOmDivide}")
	private String isShowOmDivide;


	@RequestMapping(value = "/to_uc_index")
	@IgnoredInterceptors
	public String index(String menuTree, String cookieFlag, String ticket, String app_code, String sign,
			String sign_date, String systemid, String areasystemid, HttpServletRequest req,
			HttpServletResponse response, Model model) {
		try {
			SystemUser loginUser = UcCenterUtil.parseTicket(ticket, SystemUser.class);
			req.setAttribute(PublicContains.UC_INDEX_URL, ucIndexUrl);
			HttpSession session = req.getSession();
			session.setAttribute(PublicContains.SESSION_USER, loginUser);
			session.setAttribute(PublicContains.SESSION_SYSTEMID, systemid);
			session.setAttribute(PublicContains.SESSION_AREASYSTEMID, areasystemid);
			session.setAttribute("sysVersion", sysVersion);
			session.setAttribute("isShowOmDivide", isShowOmDivide);
			
			UcCenterUtil.casheMenuTree(menuTree, session.getId());

			boolean signResult = UcCenterUtil.verifySignCert(app_code, appKey, sign_date, sign);
			if (!signResult) {
				//验证不通过
				throw new Exception("系统接入权限验证不通过.");
			}

			//设置cookie保存登录信息
			if (StringUtils.isNotEmpty(cookieFlag) && cookieFlag.equals("1")) {
				CookieUtils.addCookie(req, response, PublicContains.LOGIN_SYSTEM_USER_COOKIE_ID,
						loginUser.getLoginName(), PublicContains.LOGIN_COOKIE_TIME);
			}
			return "forward:index";
		} catch (Exception e) {
			model.addAttribute(PublicContains.ERROR_KEY, e.getMessage());
			log.error(e.getMessage(), e);
			return "other/error";
		}
	}

	@RequestMapping(value = "/uc_index")
	@IgnoredInterceptors
	public String index(HttpServletRequest req, Model model) {
		return "ucindex";
	}

	@RequestMapping(value = "/uc_user_tree.json")
	@ResponseBody
	public String ucUserTree(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			if (session != null) {
				return UcCenterUtil.getMenuTreeFromCashe(session.getId());
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 注销
	 * @throws Exception 
	 */
	@RequestMapping("/uc_logout")
	public String logout(String loginName, String loginPassword, String flag, HttpServletRequest req,
			HttpServletResponse response, Model model) {
		try {
			logoutMethod(req, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "redirect:" + ucLogoutUrl;
	}

	private void logoutMethod(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			//1.清除登录用户session
			session.removeAttribute(PublicContains.SESSION_USER);
			UcCenterUtil.removeMenuTreeFromCashe(session.getId());
		}
	}

	@RequestMapping("/setLoc")
	public void SelectLoc(String locName, String locNo, HttpServletRequest req, HttpServletResponse response,
			Model model) throws UnsupportedEncodingException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			user.setLocName(locName);
			user.setLocNo(locNo);
			session.setAttribute(PublicContains.SESSION_USER, user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	/**
	 * 系统环境区分
	 * tomcat中标识当前环境是哪一套-Denv=online
	 * dev、test、experience、online
	 * @author wanghb
	 * @date   2014-7-23
	 * @version 1.1.3.34
	 */
	@ModelAttribute("env")
	public String getEnvParam() {
		String env = System.getProperty("env");
		if ("dev".equals(env)) {
			return "(开发环境)";
		} else if ("test".equals(env)) {
			return "(测试环境)";
		} else if ("experience".equals(env)) {
			return "(体验环境)";
		} else {
			return "";
		}
	}
}
