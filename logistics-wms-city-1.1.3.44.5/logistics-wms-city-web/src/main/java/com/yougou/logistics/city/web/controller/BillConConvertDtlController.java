package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtil;
import com.yougou.logistics.city.manager.BillConConvertDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
@RequestMapping("/bill_con_convert_dtl")
public class BillConConvertDtlController extends BaseCrudController<BillConConvertDtl> {
    @Resource
    private BillConConvertDtlManager billConConvertDtlManager;
    
    @Log
	private Logger log;
    
    @Override
    public CrudInfo init() {
        return new CrudInfo("billConConvertDtl/",billConConvertDtlManager);
    }
    
    @RequestMapping(value = "/content.json")
	@ResponseBody
	public Map<String, Object> queryContentList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String itemNo = "";
			if(paramsAll.get("itemNo") != null) {
				itemNo = paramsAll.get("itemNo").toString().toUpperCase();
				params.put("itemNo", itemNo);
			}
			String barcode = "";
			if(paramsAll.get("barcode") != null) {
				barcode = paramsAll.get("barcode").toString().toUpperCase();
				params.put("barcode", barcode);
			}
			int total = this.billConConvertDtlManager.findContentCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConConvertDtl> list = this.billConConvertDtlManager.findContentByPage(page, sortColumn, sortOrder, params,
					authorityParams);

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
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDtl")
	@ResponseBody
	public Object saveDtl(HttpServletRequest req, String inserted, String deleted, String updated){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	String result = "";
    	try {
    		String deletedList = URLDecoder.decode(deleted, "UTF-8");
    		String upadtedList = URLDecoder.decode(updated, "UTF-8");
    		String insertedList = URLDecoder.decode(inserted, "UTF-8");
    		String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : req.getParameter("locno");
    		String convertNo = StringUtils.isEmpty(req.getParameter("convertNo")) ? "" : req.getParameter("convertNo");
    		String ownerNo = StringUtils.isEmpty(req.getParameter("ownerNo")) ? "" : req.getParameter("ownerNo");
    		String operator = StringUtils.isEmpty(req.getParameter("operator")) ? "" : req.getParameter("operator");
    		String destLocno = StringUtils.isEmpty(req.getParameter("destLocno")) ? "" : req.getParameter("destLocno");
    		String creatorName = StringUtils.isEmpty(req.getParameter("editorName")) ? "" : req.getParameter("editorName");
    		String editorName = StringUtils.isEmpty(req.getParameter("editorName")) ? "" : req.getParameter("editorName");
    		
    		List<BillConConvertDtl> insertList = new ArrayList<BillConConvertDtl>();
			List<BillConConvertDtl> deleteList = new ArrayList<BillConConvertDtl>();
			List<BillConConvertDtl> updatedList = new ArrayList<BillConConvertDtl>();

			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(upadtedList)) {
				List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>() {
				});
				updatedList = convertListWithTypeReference2(mapper, list);
			}
			billConConvertDtlManager.saveDtl(locno, convertNo, ownerNo, operator, destLocno, creatorName,editorName,insertList, deleteList, updatedList);
			result = "success";
		} catch (Exception e) {
			result = CommonUtil.getExceptionMessage(e);
			log.error(result, e);
		}
    	obj.put("result", result);
    	return obj;
    }
    @SuppressWarnings("rawtypes")
	private List<BillConConvertDtl> convertListWithTypeReference2(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillConConvertDtl> tl = new ArrayList<BillConConvertDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillConConvertDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillConConvertDtl.class);
			tl.add(type);
		}
		return tl;
	}
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = this.billConConvertDtlManager.findCount(params);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<BillConConvertDtl> list = this.billConConvertDtlManager.findByPage(page, sortColumn, sortOrder, params);
		Map<String, Object> obj = new HashMap<String, Object>();
		// 返回汇总列表
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		footerMap.put("cellNo", "小计");
		footerList.add(footerMap);
		for (BillConConvertDtl dtl : list) {
			SumUtil.setFooterMap("itemQty", dtl.getItemQty(), footerMap);
		}
		Map<String, Object> sumFoot = new HashMap<String, Object>();
		if (pageNo == 1) {
			if(pageSize >= total){
				sumFoot.putAll(footerMap);
			}else{
				Map<String, Object> map = billConConvertDtlManager.findSumQty(params);
				SumUtil.setSumMap(map, sumFoot);
			}
			sumFoot.put("cellNo", "总计");
			footerList.add(sumFoot);
		}
		obj.put("total", total);
		obj.put("rows", list);
		obj.put("footer", footerList);
		return obj;
	}
    @RequestMapping(value = "/printDetail4SizeHorizontal")
   	@ResponseBody   
   	public Map<String,Object> printDetail4SizeHorizontal(HttpServletRequest req,String keys)throws ManagerException{
    	Map<String, Object> result = new HashMap<String, Object>();
		
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			List<Map<String, Object>> resultList = this.billConConvertDtlManager.findDtl4SizeHorizontal(keys,authorityParams);
			result.put("pages", resultList);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
    }
}