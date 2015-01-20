package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ColorInfo;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.manager.ColorInfoManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 颜色管理
 * @author su.yq
 * @date  Tue Oct 29 11:44:54 CST 2013
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
@RequestMapping("/color_info")
@ModuleVerify("25030050")
public class ColorInfoController extends BaseCrudController<ColorInfo> {
	@Log
	private Logger log;
	
    @Resource
    private ColorInfoManager colorInfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("colorinfo/",colorInfoManager);
    }
    
    
    
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = colorInfoManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<ColorInfo> list = colorInfoManager.findByPage(page, sortColumn, sortOrder, params,authorityParams);    		
    		obj.put("total", total);
    		obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return obj;
	}
   
    
}