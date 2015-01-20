package com.yougou.logistics.city.web.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CacheTypeEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.CacheManage;
import com.yougou.logistics.base.web.interceptor.BaseModuleVerifyInterceptor;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.uc.common.api.dto.AuthorityUserModuleDto;
import com.yougou.logistics.uc.common.api.service.AuthorityUserApi;

public class ModuleVerifyInterceptor extends BaseModuleVerifyInterceptor {

	private final static String MODULE_CACHE_KEY = "moduleList";

	@Resource
	private AuthorityUserApi authorityUserApi;

	@SuppressWarnings("unchecked")
	@Override
	protected boolean bizHandleInternal(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod method = (HandlerMethod) handler;
		//模块编号如机构为21010020
		String mv = getModuleVerifyFromHandler(method);
		//需要验证的权限
		OperationVerifyEnum ove = getOperationVerifyFromHandler(method);
		try {
			//调用服务
			Object u = request.getSession().getAttribute(PublicContains.SESSION_USER);
			Object id = request.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			Map<String, AuthorityUserModuleDto> moduleMap = null;
			if (null != u && null != id) {
				SystemUser user = (SystemUser) u;
				int sid = Integer.parseInt(id.toString());
				//存缓存中的用户模块信息
				Object mo = CacheManage.get(user.getUserid() + MODULE_CACHE_KEY, CacheTypeEnum.AUTHORITY_MODULE_QUERY);
				if (null != mo) {
					moduleMap = (Map<String, AuthorityUserModuleDto>) mo;
				} else {
					List<AuthorityUserModuleDto> mList = authorityUserApi.findAllUserHasModules(user.getUserid(), sid);
					if (null != mList && mList.size() > 0) {
						Map<String, AuthorityUserModuleDto> m = new HashMap<String, AuthorityUserModuleDto>(
								mList.size());
						for (AuthorityUserModuleDto um : mList) {
							if (StringUtils.isNotEmpty(um.getModuleCode())) {
								m.put(um.getModuleCode(), um);
							}
						}
						if (m.size() > 0) {
							moduleMap = m;
							CacheManage.put(user.getUserid() + MODULE_CACHE_KEY, m,
									CacheTypeEnum.AUTHORITY_MODULE_QUERY);
						}
					}
				}
			}

			if (moduleMap == null) {
				return false;
			}
			AuthorityUserModuleDto tempModuleDto = moduleMap.get(mv);
			if (tempModuleDto == null) {
				return false;
			}
			request.setAttribute("thisPower", tempModuleDto.getPermissions());
			List<OperationVerifyEnum> opts = tempModuleDto.getOperations();
			//验证公共权限与用户选择权限判断
			String name = method.getMethod().getName();
			if ("add".equals(name) || "post".equals(name) || ove == OperationVerifyEnum.ADD) {
				return null != opts && opts.contains(OperationVerifyEnum.ADD);
			}
			if ("modify".equals(name) || "put".equals(name) || ove == OperationVerifyEnum.MODIFY) {
				return null != opts && opts.contains(OperationVerifyEnum.MODIFY);
			}
			if ("remove".equals(name) || "delete".equals(name) || ove == OperationVerifyEnum.REMOVE) {
				return null != opts && opts.contains(OperationVerifyEnum.REMOVE);
			}
			//none不验证,不是公共或私有没有注解不验证
			if (ove == OperationVerifyEnum.NONE) {
				return true;
			}
			if (ove == OperationVerifyEnum.VERIFY) {//审核权限 JYS
				return null != opts && opts.contains(OperationVerifyEnum.VERIFY);
			}
			if (ove == OperationVerifyEnum.EXPORT) {//导出权限 JYS
				return null != opts && opts.contains(OperationVerifyEnum.EXPORT);
			}
			return false;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
}
