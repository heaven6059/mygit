package com.yougou.logistics.city.service.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.service.AuthorityUserBrandService;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-3-31 下午7:52:03
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Aspect
@Order(value = 2)
@Component
public class DataAccessHandler {
	@Log
	private Logger log;
	
	@Resource
	private AuthorityUserBrandService authorityUserBrandService;

	@Before("@annotation(com.yougou.logistics.base.common.annotation.DataAccessAuth)")
	public void doBefore(JoinPoint jp) throws ServiceException {		
		if (jp.getArgs().length == 0) {
			return;
		}
		AuthorityParams params = null;
		//方法中需要验证的权限参数
		DataAccessRuleEnum[] enumArr = null;
		for (Object obj : jp.getArgs()) {
			if (obj instanceof AuthorityParams) {
				params = (AuthorityParams) obj;
			} else if (obj instanceof DataAccessRuleEnum[]) {
				enumArr = (DataAccessRuleEnum[]) obj;
			}
		}

		if (params == null) {
			return;
		}
		
		if (StringUtils.isBlank(params.getUserId())
				|| StringUtils.isBlank(params.getSystemNoVerify())
				|| StringUtils.isBlank(params.getAreaSystemNoVerify())) {
			throw new ServiceException("请重新登陆系统。");
		}
		
		//需要验证权限
		params.setVerification(true);

		//注解中需要验证的权限参数	
		DataAccessRuleEnum[] enums = this.getAnnotationParams(jp);
		//真正需要验证的权限参数(合并汇总后结果)
		Set<DataAccessRuleEnum> enumSet = new HashSet<DataAccessRuleEnum>();
		if (enumArr != null) {
			for (DataAccessRuleEnum dataAccessRuleEnum : enumArr) {
				enumSet.add(dataAccessRuleEnum);
			}
		}
		if (enums != null) {
			for (DataAccessRuleEnum dataAccessRuleEnum : enums) {
				enumSet.add(dataAccessRuleEnum);
			}
		}

		String userId = params.getUserId();
		String systemId = params.getSystemNoVerify();
		String areaSystemId = params.getAreaSystemNoVerify();
		if (params.getQueryParams() == null) {
			params.setQueryParams(new HashMap<String, List<String>>());
		}
		if (enumSet.contains(DataAccessRuleEnum.BRAND)) {
			//品牌权限
			List<String> brandNoList = authorityUserBrandService.findByUserIdBrandList(userId,Integer.valueOf(systemId),Integer.valueOf(areaSystemId));
			checkParam(params.getBrandNoVerify(), brandNoList);
			params.setHasBrandNoList(brandNoList);
			
			
			//品牌库
			List<String> partSysNoList=authorityUserBrandService.findByUserIdPartSysNoList(userId,Integer.valueOf(systemId),Integer.valueOf(areaSystemId));			
			if(partSysNoList.size()>0){
				params.getQueryParams().put("partSysNoList", partSysNoList);
			}
		}
	}
	private List<String> getSqlSysNoParamList(String no,List<String> dataList) {
		List<String> resultList = new ArrayList<String>();
		if (StringUtils.isNotBlank(no)) {
			resultList.add(no);
		}else {
			resultList.addAll(dataList);
		}
		return resultList;
	}
	
	private void checkParam(String no,Collection<String> configList) throws ServiceException {
		
		if(configList.size()==0){
			throw new ServiceException("没有数据访问权限!");
		}else if(StringUtils.isNotBlank(no)&&!configList.contains(no)){
			throw new ServiceException("illlegal brand's param");
		} 
	}
	
	@SuppressWarnings("rawtypes")
	private DataAccessRuleEnum[] getAnnotationParams(JoinPoint jp) {
		//拦截的实体类
		Object target = jp.getTarget();
		//拦截的方法名称
		String methodName = jp.getSignature().getName();				
		//拦截的方法参数
		//拦截的放参数类型
		Class[] parameterTypes = ((MethodSignature)jp.getSignature()).getMethod().getParameterTypes();
		//通过反射获得拦截的method
		Method m = null;
		try {
			m = target.getClass().getMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		DataAccessAuth auth=m.getAnnotation(DataAccessAuth.class);
		return auth.value();
	}
}