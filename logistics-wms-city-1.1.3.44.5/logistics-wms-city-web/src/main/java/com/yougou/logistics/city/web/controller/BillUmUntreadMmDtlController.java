package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmUntreadMmDtlManager;
import com.yougou.logistics.city.manager.StoreManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
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
@RequestMapping("/bill_um_untread_mm_dtl")
@ModuleVerify("25060040")
public class BillUmUntreadMmDtlController extends BaseCrudController<BillUmUntreadMmDtl> {
	@Log
	private Logger log;
	@Resource
	private BillUmUntreadMmDtlManager billUmUntreadMmDtlManager;
	@Resource
	private StoreManager storeManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmUntreadMmDtl/", billUmUntreadMmDtlManager);
	}

	@RequestMapping(value = "/selectItem")
	@ResponseBody
	public Map<String, Object> selectItem4ChCheck(HttpServletRequest req, BillUmUntreadMmDtl dtl)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			int total = billUmUntreadMmDtlManager.selectItemCount(dtl);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmUntreadMmDtl> list = billUmUntreadMmDtlManager.selectItem(page, dtl);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDetail")
	@ResponseBody
	public Map<String, Object> saveDetail(HttpServletRequest req, BillUmUntreadMm untread, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			untread.setLocno(user.getLocNo());
			untread.setCreator(user.getLoginName());
			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			inserted = URLDecoder.decode(inserted, "UTF-8");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			updated = URLDecoder.decode(updated, "UTF-8");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deleted = URLDecoder.decode(deleted, "UTF-8");

			List<BillUmUntreadMmDtl> insertList = new ArrayList<BillUmUntreadMmDtl>();
			List<BillUmUntreadMmDtl> updateList = new ArrayList<BillUmUntreadMmDtl>();
			List<BillUmUntreadMmDtl> deleteList = new ArrayList<BillUmUntreadMmDtl>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(updated)) {
				List<Map> list = mapper.readValue(updated, new TypeReference<List<Map>>() {
				});
				updateList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}
			billUmUntreadMmDtlManager.saveUntreadMmDtl(insertList, updateList, deleteList, untread);
			resultMap.put("result", ResultEnums.SUCCESS.getResultMsg());
			return resultMap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}

	@SuppressWarnings("rawtypes")
	private List<BillUmUntreadMmDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmUntreadMmDtl> tl = new ArrayList<BillUmUntreadMmDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmUntreadMmDtl type = mapper
					.readValue(mapper.writeValueAsString(list.get(i)), BillUmUntreadMmDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/queryStoreByStoreNo")
	@ResponseBody
	public Map<String, Object> queryStoreByStoreNo(HttpServletRequest req, Store store, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String storeNo = storeManager.queryStoreByStoreNo(store);
			resultMap.put("storeNo", storeNo);
			return resultMap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			return resultMap;
		}
	}

	@RequestMapping(value = "/selectStoreNo")
	@ResponseBody
	public Map<String, Object> selectStoreNo(HttpServletRequest req, BillUmUntreadMm mm, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<BillUmUntreadMmDtl> list = billUmUntreadMmDtlManager.selectStoreNo(mm);
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return resultMap;
		}
	}
	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billUmUntreadMmDtlManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmUntreadMmDtl> list = billUmUntreadMmDtlManager.findByPage(page, sortColumn, sortOrder, params);
			
			obj.put("total", total);
			obj.put("rows", list);
//		Map<String, Object> obj = super.queryList(req, model);
//		List<BillUmUntreadMmDtl> list = CommonUtil.getRowsByListJson(obj, BillUmUntreadMmDtl.class);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			for(BillUmUntreadMmDtl dtl:list){
				totalItemQty = totalItemQty.add(dtl.getItemQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("itemNo", "小计");
			footer.put("itemQty", totalItemQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmUntreadMmDtlManager.selectSumQty(params);
				if (sumFoot != null) {
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("itemNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
}