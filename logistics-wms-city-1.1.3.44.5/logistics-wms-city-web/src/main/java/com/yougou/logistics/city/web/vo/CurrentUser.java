package com.yougou.logistics.city.web.vo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;

/**
 * @存放当前用户信息
 * @author wei.hj
 * @Date 2013-7-29
 * @version 0.1.0
 * @copyright yougou.com
 */
public class CurrentUser {

	private Integer userid;//用户ID
	private String username; // 中文姓名  管理员
	private String loginName; // 登陆名称  admin 
	private String locno; //仓别
	private String locname;
	private String currentDate10Str; // yyyy-MM-dd
	private String currentDate19Str; // yyyy-MM-dd HH:mm:ss

	public CurrentUser() {
		super();
	}

	public CurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		this.setUsername(user.getUsername());
		this.setLoginName(user.getLoginName());
		this.setUserid(user.getUserid());
		this.setLocno(user.getLocNo());
		this.setLocname(user.getLocName());
	}

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
	 * 2.获取当前登录用户的菜单权限列表
	 * @param request
	 * @return
	 */

	/**
	 * 3.获取当前登录用户的数据权限列表
	 * @param request
	 * @return
	 */

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCurrentDate10Str() {
		return CommonUtil.getCurrentDate();
	}

	public void setCurrentDate10Str(String currentDate10Str) {
		this.currentDate10Str = currentDate10Str;
	}

	public String getCurrentDate19Str() {
		return CommonUtil.getCurrentDateTime();
	}

	public void setCurrentDate19Str(String currentDate19Str) {
		this.currentDate19Str = currentDate19Str;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getLocname() {
		return locname;
	}

	public void setLocname(String locname) {
		this.locname = locname;
	}

}
