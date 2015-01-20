package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
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
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmUntreadDtlManager;
import com.yougou.logistics.city.manager.BillUmUntreadManager;
import com.yougou.logistics.city.manager.ConBoxDtlManager;
import com.yougou.logistics.city.manager.StoreManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
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
@RequestMapping("/bill_um_untread")
@ModuleVerify("25060050")
public class BillUmUntreadController extends BaseCrudController<BillUmUntread> {
	@Log
	private Logger log;
	@Resource
	private BillUmUntreadManager billUmUntreadManager;

	@Resource
	private BillUmUntreadDtlManager billUmUntreadDtlManager;

	@Resource
	private StoreManager storeManager;

	@Resource
	private ConBoxDtlManager conBoxDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billumuntread/", billUmUntreadManager);
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
			int total = this.billUmUntreadManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmUntread> list = this.billUmUntreadManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			//小计
			Map<String, Object> footer = new HashMap<String, Object>();
            List<Object> footerList = new ArrayList<Object>();
			long totalItemQty = 0;
			long totalReceiptQty = 0;
			long totalCheckQty = 0;
            for (BillUmUntread billUmUntread : list) {
                totalItemQty += billUmUntread.getItemQty();
                totalReceiptQty += billUmUntread.getReceiptQty();
                totalCheckQty += billUmUntread.getCheckQty();
            }
            footer.put("untreadNo", "小计");
            footer.put("itemQty", totalItemQty);
            footer.put("receiptQty", totalReceiptQty);
            footer.put("checkQty", totalCheckQty);
            footerList.add(footer);
            //合计
            Map<String, Object> sumFoot = new HashMap<String, Object>();
            if (pageNo == 1) {
                sumFoot = this.billUmUntreadManager.selectSumQty(params, authorityParams);
                if (sumFoot == null) {
                    sumFoot = new SumUtilMap<String, Object>();
                    sumFoot.put("item_qty", 0);
                    sumFoot.put("receipt_qty", 0);
                    sumFoot.put("check_qty", 0);
                }
                sumFoot.put("isselectsum", true);
                sumFoot.put("untread_no", "合计");
            } else {
                sumFoot.put("untreadNo", "合计");
            }
            footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
	
	@RequestMapping(value = "/findUntread2CheckTask.json")
	@ResponseBody
	public Map<String, Object> findUntread2CheckTask(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			List<BillUmUntread> untreadList = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				untreadList = convertListWithTypeReference(mapper, list);
			}
			
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			//int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			//int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			SimplePage page = new SimplePage(0, 0, (int) 0);
			List<BillUmUntread> list = billUmUntreadManager.findUntread2CheckTask(page, sortColumn, sortOrder, params, untreadList, authorityParams);
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

	// 新增主表
	@RequestMapping(value = "/addMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> addMain(BillUmUntread untreadMm, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadManager.saveMain(untreadMm, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("untreadNo", untreadMm.getUntreadNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	// 修改主表
	@RequestMapping(value = "/eidtMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> eidtMain(BillUmUntread untreadMm, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadManager.saveMain(untreadMm, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("untreadNo", untreadMm.getUntreadMmNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("msg", e.getMessage());
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	// 删除
	@RequestMapping(value = "/deleteUntread")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteUntreadMm(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadManager.deleteUntread(keyStr, user.getLocNo());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("msg", e.getMessage());
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	// 审核
	@RequestMapping(value = "/auditUntread")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditUntreadMm(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmUntreadManager.auditUntread(keyStr, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("msg", "审核失败，请联系管理员");
		}
		return map;
	}

	// 查询客户
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value = "/selectStore")
	@ResponseBody
	public Map<String, Object> selectStore(HttpServletRequest req, Model model, Store store) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map params = builderParams(req, model);
			int total = storeManager.queryStoreCount(store);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Store> list = storeManager.queryStoreList(store, page);

			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	// 选择箱子
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectBox")
	@ResponseBody
	public Map<String, Object> selectBox(HttpServletRequest req, Model model, Store store) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map params = builderParams(req, model);
			int total = conBoxDtlManager.selectItem4umuntreadCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<ConBoxDtl> list = conBoxDtlManager.selectItem4umuntread(params, page, authorityParams);

			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/pringBatch")
	@ResponseBody
	public Map<String, Object> pringBatch(HttpServletRequest req, Model model, HttpSession session, String keystr)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			List<Map<String, Object>> list = this.billUmUntreadDtlManager.queryPrints(user.getLocNo(), keystr);
			obj.put("list", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	/**
	 * 作废店退仓单 
	 * @param req
	 * @param untreadMm
	 * @return
	 * @throws Exception
	 * @author wanghb
	 */
	@RequestMapping(value = "/invalid")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.VERIFY)
    public Object invalid(HttpServletRequest req,BillUmUntread untreadMm) throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
    	String untReadNostr = StringUtils.isEmpty(req.getParameter("untReadNos")) ? "" : req.getParameter("untReadNos");
    	if(StringUtils.isBlank(untReadNostr)){
    		map.put("result", "请选择作废的记录");
    	}else{
    		try {
    			map=billUmUntreadManager.invalid(untreadMm,untReadNostr,user);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				map.put("result", "error");
			}
    	}
    	
    	return map;
    }
	/**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillUmUntread> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<BillUmUntread> entityClass = (Class<BillUmUntread>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<BillUmUntread> tl = new ArrayList<BillUmUntread>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmUntread type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}
}