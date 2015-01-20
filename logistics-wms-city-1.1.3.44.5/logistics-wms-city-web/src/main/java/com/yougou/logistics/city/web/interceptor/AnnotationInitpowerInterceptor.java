package com.yougou.logistics.city.web.interceptor;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.AuthorityRoleModuleManager;

public class AnnotationInitpowerInterceptor implements HandlerInterceptor {

	private String sessionKey;
	private String redirectUrl;

	@Resource
	private AuthorityRoleModuleManager authorityRoleModuleManager;

	private static final String LOCNO = "locno";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//用于区分mvc:resources, 正常的Controller请求
		if (handler == null || !HandlerMethod.class.isAssignableFrom(handler.getClass())) {
			return true;
		}
		Enumeration enu = request.getParameterNames();
		String locno = null;
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			if (LOCNO.equalsIgnoreCase(paraName)) {
				locno = request.getParameter(paraName);
			}
		}
		SystemUser user = (SystemUser) request.getSession().getAttribute(sessionKey);
		if (user != null && user.getLocNo() != null && (locno != null && !user.getLocNo().equals(locno))) {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<script type=\"text/javascript\">");
			out.println("window.open ('" + redirectUrl + "','_top')");
			out.println("</script>");
			out.println("</html>");
			return false;
		}
		if (isInitpower(handler)) {
			//安全验证
			if (null == user || StringUtils.isEmpty(user.getLocNo())) {
				response.sendRedirect(redirectUrl);
			} else if (StringUtils.isEmpty(user.getLocNo())) {
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<script type=\"text/javascript\">");
				out.println("window.open ('" + redirectUrl + "','_top')");
				out.println("</script>");
				out.println("</html>");
				return false;
			} else {
				Integer userId = user.getUserid();
				String moduleId = request.getParameter("moduleId");
				if (CommonUtil.hasValue(moduleId)) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userId", userId);
					map.put("moduleId", Long.valueOf(moduleId));
					AuthorityRoleModule en = new AuthorityRoleModule();
					List<AuthorityRoleModule> list = this.authorityRoleModuleManager.findByBiz(en, map);
					int thisPower = 0;
					if (CommonUtil.hasValue(list)) {
						for (AuthorityRoleModule vo : list) {
							if (vo != null && vo.getOperPermissions() != null) {
								thisPower = (thisPower | vo.getOperPermissions());
							}
						}
					}
					request.setAttribute("thisPower", thisPower);
				}
			}

			return true;
		} else {
			return true;
		}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private boolean isInitpower(Object handler) {
		HandlerMethod method = (HandlerMethod) handler;
		InitpowerInterceptors initpower = method.getMethodAnnotation(InitpowerInterceptors.class);
		if (initpower == null) {
			initpower = handler.getClass().getAnnotation(InitpowerInterceptors.class);
		}

		return (initpower == null) ? false : true;
		//Class<? extends HandlerInterceptor>[] interceptors = ignored.value();
		//return ArrayUtils.contains(interceptors, this.getClass());
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
