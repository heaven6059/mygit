package com.yougou.logistics.city.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmContainerLog;
import com.yougou.logistics.city.manager.BmContainerLogManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 容器操作日志管理
 * @author wanghb
 * @date   2014-8-16
 * @version 1.1.3.39
 */
@Controller
@RequestMapping("/bc_log")
public class BmContainerLogController extends BaseCrudController<BmContainerLog> {
    @Resource
    private BmContainerLogManager bmContainerLogManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmcontainerlog/",bmContainerLogManager);
    }
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = bmContainerLogManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<BmContainerLog> list =bmContainerLogManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		return obj;
	}
    @RequestMapping(value="/dtl_list")
 	@ResponseBody
 	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = bmContainerLogManager.findDtlCount(params);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<BmContainerLog> list = bmContainerLogManager.findDtlByPage(page, sortColumn, sortOrder, params);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		return obj;
	}
}