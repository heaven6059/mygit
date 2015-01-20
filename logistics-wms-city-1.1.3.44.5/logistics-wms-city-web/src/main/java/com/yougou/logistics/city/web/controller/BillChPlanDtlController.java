package com.yougou.logistics.city.web.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillChPlanDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
@RequestMapping("/bill_ch_plan_dtl")
public class BillChPlanDtlController extends BaseCrudController<BillChPlanDtl> {

    @Log
	private Logger log;
    
    @Resource
    private BillChPlanDtlManager billChPlanDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchplandtl/",billChPlanDtlManager);
    }
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
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
			Map<String, Object> params = builderParams(req, model);
			/////////////////////商品盘、储位盘分支 S/////////////////////
			String planType = params.get("planType")==null?"":params.get("planType").toString();
			int total = 0;
			SimplePage page = null;
			List<BillChPlanDtl> list = null;
			if(StringUtils.isBlank(planType)){
				throw new Exception("请输入盘点类型!");
			}else{
				if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
					total = this.billChPlanDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
					page = new SimplePage(pageNo, pageSize, (int) total);
					list = this.billChPlanDtlManager.findByPage(page, sortColumn, sortOrder, params,
							authorityParams, DataAccessRuleEnum.BRAND);
				}else if(BillChPlanTypeEnums.CELL.getValue().equals(planType)){
					total = this.billChPlanDtlManager.findCount(params);
					page = new SimplePage(pageNo, pageSize, (int) total);
					list = this.billChPlanDtlManager.findByPage(page, sortColumn, sortOrder, params);
				}
			}
			/////////////////////商品盘、储位盘分支 E/////////////////////

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
    @RequestMapping(value = "/save_dtl")
	@ResponseBody
    public Object saveDtl(BillChPlan billChPlan,HttpServletRequest req){
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	try {
			String planType = billChPlan.getPlanType();
			String planNo = billChPlan.getPlanNo();
			String locno = billChPlan.getLocno();
			billChPlan.setOwnerNo("BL");
			String ownerNo = billChPlan.getOwnerNo();
			if(StringUtils.isBlank(planType)){
				result = "缺少盘点类型!";
			}else if(StringUtils.isBlank(planNo)){
				result = "缺少盘点计划单号!";
			}else if(StringUtils.isBlank(locno)){
				result = "缺少仓库编码!";
			}else if(StringUtils.isBlank(ownerNo)){
				result = "缺少货主编码!";
			}else{				
				ObjectMapper mapper = new ObjectMapper();
				String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
				String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
				
				List<Map<String, Object>> tempDeletedList = mapper.readValue(deleted, new TypeReference<List<Map<String, Object>>>(){});
				List<BillChPlanDtl> deletedList = new ArrayList<BillChPlanDtl>();
				deletedList = convertListWithTypeReference(mapper,tempDeletedList,BillChPlanDtl.class);
				
				List<Map<String, Object>> tempInsertedList = mapper.readValue(inserted, new TypeReference<List<Map<String, Object>>>(){});
				List<BillChPlanDtl> insertedList = new ArrayList<BillChPlanDtl>();
				insertedList = convertListWithTypeReference(mapper,tempInsertedList,BillChPlanDtl.class);
				
				billChPlanDtlManager.save(billChPlan, insertedList , deletedList);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
		}
    	map.put("result", result);
    	return map;
    }
    @RequestMapping(value = "/save_dtl_batch")
	@ResponseBody
    public Object saveDtlBatch(BillChPlan billChPlan,HttpServletRequest req){
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	try {
			String planType = billChPlan.getPlanType();
			String planNo = billChPlan.getPlanNo();
			String locno = billChPlan.getLocno();
			billChPlan.setOwnerNo("BL");
			String ownerNo = billChPlan.getOwnerNo();
			String wareNo = req.getParameter("wareNo");
			String areaNo = req.getParameter("areaNo");
			String stockNo = req.getParameter("stockNo");
			if(StringUtils.isBlank(planType)){
				result = "缺少盘点类型!";
			}else if(StringUtils.isBlank(planNo)){
				result = "缺少盘点计划单号!";
			}else if(StringUtils.isBlank(locno)){
				result = "缺少仓库编码!";
			}else if(StringUtils.isBlank(ownerNo)){
				result = "缺少货主编码!";
			}else if(StringUtils.isBlank(wareNo)){
				result = "缺少仓区编码!";
			}else if(StringUtils.isBlank(areaNo)){
				result = "缺少库区编码!";
			}else{				
				/*ObjectMapper mapper = new ObjectMapper();
				String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
				String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
				
				List<Map<String, Object>> tempDeletedList = mapper.readValue(deleted, new TypeReference<List<Map<String, Object>>>(){});
				List<BillChPlanDtl> deletedList = new ArrayList<BillChPlanDtl>();
				deletedList = convertListWithTypeReference(mapper,tempDeletedList,BillChPlanDtl.class);
				
				List<Map<String, Object>> tempInsertedList = mapper.readValue(inserted, new TypeReference<List<Map<String, Object>>>(){});
				List<BillChPlanDtl> insertedList = new ArrayList<BillChPlanDtl>();
				insertedList = convertListWithTypeReference(mapper,tempInsertedList,BillChPlanDtl.class);
				
				billChPlanDtlManager.save(billChPlan, insertedList , deletedList);*/
				billChPlanDtlManager.saveDtlBatch(billChPlan, wareNo, areaNo, stockNo);
				result = "success";
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
		}
    	map.put("result", result);
    	return map;
    }
    @RequestMapping(value = "/save_dtl_brand")
	@ResponseBody
    public Object saveDtlByBrand(BillChPlan billChPlan,HttpServletRequest req){
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	try {
			String planType = billChPlan.getPlanType();
			String planNo = billChPlan.getPlanNo();
			String locno = billChPlan.getLocno();
			billChPlan.setOwnerNo("BL");
			String ownerNo = billChPlan.getOwnerNo();
			String brandNo = billChPlan.getBrandNo();
			if(StringUtils.isBlank(planType)){
				result = "缺少盘点类型!";
			}else if(StringUtils.isBlank(planNo)){
				result = "缺少盘点计划单号!";
			}else if(StringUtils.isBlank(locno)){
				result = "缺少仓库编码!";
			}else if(StringUtils.isBlank(ownerNo)){
				result = "缺少货主编码!";
			}else if(StringUtils.isBlank(brandNo)){
				result = "缺少品牌编码!";
			}else{
				billChPlanDtlManager.saveByBrand(billChPlan);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
		}
    	map.put("result", result);
    	return map;
    }
    private <T> List<T> convertListWithTypeReference(ObjectMapper mapper,List<Map<String, Object>> list,Class<T> c) throws Exception{
		
    	List<T> tl=new ArrayList<T>(list.size());
    	Field [] fields = c.getDeclaredFields();
    	Class<?> type;
    	Object value = null;
    	String setter = "";
    	String fieldName = "";
    	Method method;
		for (Map<String, Object> map : list) {
			T t = c.newInstance();
			for(Field f:fields){
				type = f.getType();
				
				fieldName = f.getName();
				value = map.get(fieldName);
				if(type.getSimpleName().equals("Long")){
					String lv=String.valueOf(value);
					if(CommonUtil.validateLong(lv)){
						value=Long.valueOf(lv);
					}else{
						value=null;
					}
				}
				setter = "set"+String.valueOf(fieldName.charAt(0)).toUpperCase();
				setter += fieldName.substring(1);
				method = c.getMethod(setter, new Class [] {type});
				method.invoke(t, new Object[] {value});
			}
			tl.add(t);
		}
		return tl;
	}
    
    public List<BillChPlanDtl> removeRepeat(List<BillChPlanDtl> list)throws Exception{
    	String error = "";
    	String value = null;
    	StringBuffer sb = new StringBuffer();
    	ObjectMapper objectMapper = new ObjectMapper();
    	int index = 0;
    	List<BillChPlanDtl> newList = null;
    	if(list == null || list.size() == 0){
    		return list;
    	}
    	BillChPlanDtl dtl = list.get(0);
    	BillChPlanDtl temp;
    	if(!StringUtils.isBlank(dtl.getCellNo()) && !dtl.getCellNo().equals("N")){
    		newList = new ArrayList<BillChPlanDtl>();
    		for(BillChPlanDtl t:list){
    			temp = new BillChPlanDtl();
    			temp.setCellNo(t.getCellNo());
    			newList.add(temp);
    		}
		}else if(!StringUtils.isBlank(dtl.getItemNo()) && !dtl.getItemNo().equals("N")){
			newList = new ArrayList<BillChPlanDtl>();
    		for(BillChPlanDtl t:list){
    			temp = new BillChPlanDtl();
    			temp.setItemNo(t.getItemNo());
    			temp.setSizeNo(t.getSizeNo());
    			newList.add(temp);
    		}
		}
    	for(BillChPlanDtl t:newList){
    		value = objectMapper.writeValueAsString(t);
    		index = sb.indexOf(value);
    		if(index < 0){
    			sb.append(value);
    		}else{
				if(!StringUtils.isBlank(t.getCellNo()) && !t.getCellNo().equals("N")){
					error = "储位["+t.getCellNo()+"]存在多条重复,保存失败!";
				}else if(!StringUtils.isBlank(t.getItemNo()) && !t.getItemNo().equals("N")){
					error = "商品尺码{"+t.getItemNo()+","+t.getSizeNo()+"}存在多条重复,保存失败!";
				}
				throw new Exception(error);
    		}
    	}
    	return list;
    }
   
    public String removeRepeat(String insertedList){
		StringBuffer sb = new StringBuffer();
    	String temp = insertedList;
    	temp = temp.substring(1, temp.length()-1);
    	if(!temp.trim().equals("")){
    		int index = 0;
    		Set<String> set = new HashSet<String>();
    		while(true){
    			index = temp.indexOf("}");
    			if(index < 0){
    				break;
    			}else{
    				set.add(temp.substring(0,index+1));
    				temp = temp.substring(index+1);
    				if(temp.indexOf(",") == 0){
    					temp = temp.substring(1);
    				}
    			}
    		}
    		for(String str : set){
    			sb.append(","+str);
    		}
    		temp = sb.toString().substring(1);
    		temp = "[" + temp + "]";
    	}else{
    		return insertedList;
    	}
    	return temp;
    }
}