package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
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
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.model.SupplierSimple;
import com.yougou.logistics.city.manager.SupplierManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/supplier")
@ModuleVerify("25030100")
public class SupplierController extends BaseCrudController<Supplier> {
	@Log
	private Logger log;
	
	@Resource
	private SupplierManager supplierManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("supplier/", supplierManager);
	}
	
	/**
	 * 增加品牌权限 重写base查询方法
	 * modified by liu.t  20140414
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.supplierManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Supplier> list = this.supplierManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	

	@RequestMapping(value = "/get_all")
	@ResponseBody
	public List<SupplierSimple> getBiz(HttpServletRequest req, Model model) throws ManagerException {
		List<SupplierSimple> list = new ArrayList<SupplierSimple>();
		try {
			Map<String, Object> params = builderParams(req, model);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			list = this.supplierManager.selectAll4Simple(params, authorityParams);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	
	@RequestMapping(value = "/getSupplierByRecede")
	@ResponseBody
	public List<SupplierSimple> getSupplierByRecede(HttpServletRequest req, Model model) throws ManagerException {
		List<SupplierSimple> list = new ArrayList<SupplierSimple>();
		try {
			Map<String, Object> params = builderParams(req, model);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			list = this.supplierManager.findSupplierByRecede(params, authorityParams);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
}