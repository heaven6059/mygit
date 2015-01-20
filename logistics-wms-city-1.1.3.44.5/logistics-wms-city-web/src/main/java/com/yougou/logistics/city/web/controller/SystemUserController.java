package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.CacheTypeEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.CacheManage;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.SystemUserDto;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.SystemUserManager;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 11:32:16 CST 2013
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
@RequestMapping("/system_user")
public class SystemUserController extends BaseCrudController<SystemUser> {
	
	@Log
	private Logger log;
	
    @Resource
    private SystemUserManager systemUserManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("systemUser/",systemUserManager);
    }
    
    /**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/listSystemUser.json")
	@ResponseBody
	public  Map<String, Object> listSystemUser(HttpServletRequest req, SystemUserDto systemUserDto) throws ManagerException {
		try{
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			int total = systemUserManager.findSystemUserCount(systemUserDto);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<SystemUserDto> list = systemUserManager.findSystemUserByPage(page, systemUserDto);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询系统用户列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	/**
   	 * 清空缓存
   	 */
   	@RequestMapping(value = "/initCache")
   	@ResponseBody
   	public  Object initCache(HttpServletRequest req, SystemUserDto systemUserDto) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		String result = "缓存清除成功!";
   		try{
   			CacheManage.clear(CacheTypeEnum.AUTHORITY_MENU_TREE_QUERY);
   			CacheManage.clear(CacheTypeEnum.AUTHORITY_MODULE_QUERY);
   		}catch (Exception e) {
   			log.error("==========="+e.getMessage(),e);
   			result = "缓存清除失败,请联系管理员!";
   		}
   		obj.put("result", result);
   		return obj;
   	}
}