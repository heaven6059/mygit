package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/item")
@ModuleVerify("25030130")
public class ItemController extends BaseCrudController<Item> {
	
	@Log
	private Logger log;
	
    @Resource
    private ItemManager itemManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("item/",itemManager);
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
   			int total = itemManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
   			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
   			List<Item> list = itemManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
   			obj.put("total", total);
   			obj.put("rows", list);
   			obj.put("result", "success");
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			obj.put("rows", "");
   			obj.put("result", "fail");
   			obj.put("msg", e.getCause().getMessage());
   		}
   		return obj;
   	}
	@RequestMapping(value = "/findItemAndSize")
	public ResponseEntity<Map<String,Object>> findItemAndSize(Item item, HttpServletRequest req,HttpServletResponse response) {
    	Map<String,Object> obj=new HashMap<String,Object>();
    	int total=0;
		List<Item> listItem = new ArrayList<Item>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			
			//总数
			total = itemManager.countItemAndSize(item, authorityParams);
			
			//记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			listItem = itemManager.findItemAndSizePage(page, item, authorityParams);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		obj.put("total", total);
		obj.put("rows", listItem);
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/queryItemByCateNos")
	@ResponseBody
	public  Map<String, Object> queryItemByCateNos(HttpServletRequest req, Model model,String cateNos) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.itemManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Item> list = this.itemManager.findByPage(page, sortColumn, sortOrder, params);

			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}