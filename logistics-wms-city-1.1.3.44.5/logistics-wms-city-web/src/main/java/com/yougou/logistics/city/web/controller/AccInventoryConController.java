package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.common.model.AccInventoryCon;
import com.yougou.logistics.city.common.model.AccInventorySku;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.AccContainerSkuManager;
import com.yougou.logistics.city.manager.AccInventoryConManager;
import com.yougou.logistics.city.manager.AccInventorySkuManager;
import com.yougou.logistics.city.manager.TmpConBoxExcelManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/acc_con")
public class AccInventoryConController extends BaseCrudController<AccInventoryCon> {
	@Log
	private Logger log;
    @Resource
    private AccInventoryConManager accInventoryConManager;
    @Resource
    private AccContainerSkuManager accContainerSkuManager;
    @Resource
    private AccInventorySkuManager accInventorySkuManager;
    @Resource
    private TmpConBoxExcelManager tmpConBoxExcelManager;
    
    @Override
    public CrudInfo init() {
        return new CrudInfo("accInventoryCon/",accInventoryConManager);
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
		int total = accInventoryConManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<AccInventoryCon> list = accInventoryConManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		return obj;
	}
    @RequestMapping(value = "/dtl_list")
	@ResponseBody
    public  Map<String, Object>  findAccConDtlList(HttpServletRequest req,Model model){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		String conType=(String) params.get("conType");
    		List<AccContainerSku> list;
    		int total;
    		if(conType.equals("C")){
    			total =accContainerSkuManager.findCount(params);
        		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
        		list =accContainerSkuManager.findByPage(page, sortColumn, sortOrder, params);
    		}else{
    			total =accContainerSkuManager.findPlateCount(params);
        		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
        		list =accContainerSkuManager.findPlateBypage(page, sortColumn, sortOrder, params);
    		}
    		obj.put("total", total);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error("查询容器日志异常"+e);
    		obj.put("total", 0);
    		obj.put("rows", null);
    	}
    	return obj;
    }
    @RequestMapping(value = "/sku_list")
	@ResponseBody
    public ModelAndView sku() throws Exception {
		return new ModelAndView("accInventoryCon/sku_list");
	}
    @RequestMapping(value = "/getSkuList")
	@ResponseBody
    public  Map<String, Object>  findAccConSkuList(HttpServletRequest req,Model model){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = accInventorySkuManager.findCount(params);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<AccInventorySku> list = accInventorySkuManager.findByPage(page, sortColumn, sortOrder, params);
    		obj.put("total", total);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error("查询sku 储位库存异常"+e);
    		obj.put("total", 0);
    		obj.put("rows", null);
    	}
    	return obj;
    }
    @RequestMapping(value = "/iframe")
	public ModelAndView iframe(HttpServletRequest req) throws Exception {
		ModelAndView mode = new ModelAndView("accInventoryCon/iframe");
		return mode;
	}
    @RequestMapping(value = "/importExcel")
	public ModelAndView upLoad(HttpServletRequest request,Model model) {
		ModelAndView mode = new ModelAndView("accInventoryCon/iframe");
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"locNo","cellNo","conNo","itemNo","barcode","sizeNo","qty"};
	    	String [] keyNames= {"locNo","conNo","itemNo","barcode"};
	    	boolean [] mustArray = {true,true,true,true,true,true,true};
			List<TmpConBoxExcel> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, keyNames, TmpConBoxExcel.class);
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 Map<String,Object> map=tmpConBoxExcelManager.importConBoxExcel(list,user);
			 mode.addObject("result",map.get("result"));
			 mode.addObject("msg",map.get("msg"));
			 mode.addObject("uuId", map.get("uuId"));
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		}
		return mode;
	}
    @RequestMapping(value = "/excel_preview")
    @ResponseBody
    public  Map<String, Object>  excelPreview(HttpServletRequest req,Model model){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		String uuId=(String) params.get("uuId");
    		int total = tmpConBoxExcelManager.findCount(params);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<AccInventorySku> list = tmpConBoxExcelManager.findByPage(page, sortColumn, sortOrder, params);
    		obj.put("total", total);
    		obj.put("uuId", uuId);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error("查询Excel预览异常"+e);
    		obj.put("total", 0);
    		obj.put("rows", null);
    	}
    	return obj;
    }
    @RequestMapping(value = "/clearExcelTemp")
    @ResponseBody
    public  Map<String, Object>  clearExcelTemp(HttpServletRequest req,Model model){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		Map<String,Object> params=builderParams(req, model);
    		String uuId=(String) params.get("uuId");
    		tmpConBoxExcelManager.deleteByUuid(uuId);
    		obj.put("uuId", uuId);
    	}catch(Exception e){
    		log.error("清空Excel预览数据异常"+e);
    	}
    	return obj;
    }
    @RequestMapping(value = "/saveExcelTemp")
    @ResponseBody
    public  Map<String, Object>  saveExcelTemp(HttpServletRequest req,Model model){
    	Map<String, Object> map = new HashMap<String, Object>();
    	Map<String,Object> params=builderParams(req, model);
    	String uuId=(String) params.get("uuId");
    	try{
    		SystemUser user = (SystemUser)req.getSession().getAttribute(PublicContains.SESSION_USER);
    		map=tmpConBoxExcelManager.batchSaveConBoxExcel(params,user);
    	}catch(Exception e){
    		log.error("查询Excel预览异常"+e);
    		map.put("result", "fail");
    		map.put("msg", "查询Excel预览异常"+e);
    	}
    	map.put("uuId", uuId);
    	return map;
    }
    @RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "conBoxTemplate.xls");
	}
}