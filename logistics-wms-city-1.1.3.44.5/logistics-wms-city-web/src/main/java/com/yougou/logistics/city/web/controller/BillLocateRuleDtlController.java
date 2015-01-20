package com.yougou.logistics.city.web.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.manager.BillLocateRuleDtlManager;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
@RequestMapping("/bill_locate_rule_dtl")
public class BillLocateRuleDtlController extends BaseCrudController<BillLocateRuleDtl> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillLocateRuleDtlManager billLocateRuleDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billLocateRuleDtl/",billLocateRuleDtlManager);
    }
    
    /**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/listCategoryFilter.json")
	@ResponseBody
	public  Map<String, Object> listCategoryFilter(BillLocateRuleDtl billLocateRuleDtl,Category category,
			HttpServletRequest req) throws ManagerException {
		try{
			
			List<BillLocateRuleDtl> listInserteds = new ArrayList<BillLocateRuleDtl>();
			List<BillLocateRuleDtl> listDelteds = new ArrayList<BillLocateRuleDtl>();
			
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
				listInserteds=convertListWithTypeReference(mapper,list);
			}
			
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
				listDelteds=convertListWithTypeReference(mapper,list);
			}
			
			int total = billLocateRuleDtlManager.findCategoryFilterCount(billLocateRuleDtl, category, listDelteds, listInserteds);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Category> list = billLocateRuleDtlManager.findCategoryFilterByPage(page, billLocateRuleDtl, category, listDelteds, listInserteds);
			
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询调度波次列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	
   	private List<BillLocateRuleDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillLocateRuleDtl> tl = new ArrayList<BillLocateRuleDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillLocateRuleDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillLocateRuleDtl.class);
			tl.add(type);
		}
		return tl;
	}
}