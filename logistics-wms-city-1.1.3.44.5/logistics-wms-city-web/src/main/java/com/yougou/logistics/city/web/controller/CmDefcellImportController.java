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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpCmDefCellExcel;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.CmDefcellImportManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;

@Controller
@RequestMapping("/cmdefcell_import")
public class CmDefcellImportController extends BaseCrudController<TmpCmDefCellExcel> {
	@Log
	private Logger log;
	@Resource
	private CmDefcellImportManager cmDefcellImportManager;
    
    @Override
    public CrudInfo init() {
    	return new CrudInfo("cmdefcell/", cmDefcellImportManager);
    }
    @RequestMapping(value = "/iframe")
	public ModelAndView iframe(HttpServletRequest req) throws Exception {
		ModelAndView mode = new ModelAndView("cmdefcell/iframe");
		return mode;
	}
    @RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "cmDefcellTemplate.xls");
	}
    @RequestMapping(value = "/importExcel")
	public ModelAndView upLoad(HttpServletRequest request,Model model) {
		ModelAndView mode = new ModelAndView("cmdefcell/iframe");
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"wareNo","wareName","areaNo","areaName","areaType","areaAttribute","" +
	    			"areaUsetype","stockNo","stockName","stockX","bayX","stockY","cellNo","itemType",
	    			"quality","mixFlag"};
	    	String [] keyNames= {"wareNo","wareNo","stockNo","cellNo"};//主键
	    	boolean [] mustArray = {true,false,true,false,true,true,true,true,true,true,
	    			true,true,true,true,true,true};//必填
			List<TmpCmDefCellExcel> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, keyNames, TmpCmDefCellExcel.class);
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 Map<String,Object> map=cmDefcellImportManager.importConBoxExcel(list,user);
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
    		int total = cmDefcellImportManager.findCount(params);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<TmpCmDefCellExcel> list = cmDefcellImportManager.findByPage(page, sortColumn, sortOrder, params);
    		obj.put("total", total);
    		obj.put("uuId", uuId);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error("查询储位导入Excel预览异常"+e);
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
    		cmDefcellImportManager.deleteByUuid(uuId);
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
    		map=cmDefcellImportManager.batchSaveExcel(params,user);
    	}catch(Exception e){
    		log.error("查询Excel预览异常"+e);
    		map.put("result", "fail");
    		map.put("msg", "");
    	}
    	map.put("uuId", uuId);
    	return map;
    }
}