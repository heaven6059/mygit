package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
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
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmOutstockDtlManager;
import com.yougou.logistics.city.manager.BillOmOutstockManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/*
 * 拣货单 
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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
@RequestMapping("/bill_om_outstock")
@ModuleVerify("25080050")
public class BillOmOutstockController extends BaseCrudController<BillOmOutstock> {
	@Resource
	private BillOmOutstockManager billOmOutstockManager;
	@Resource
	private BillOmOutstockDtlManager billOmOutstockDtlManager;
	@Log
    private Logger log;
	@Override
	public CrudInfo init() {
		return new CrudInfo("billomoutstock/", billOmOutstockManager);
	}

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
			int total = this.billOmOutstockManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstock> list = this.billOmOutstockManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			// 返回小计列表
		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		    Map<String, Object> footerMap = new HashMap<String, Object>();
		    footerMap.put("outstockNo", "小计");
		    footerList.add(footerMap);
		    for (BillOmOutstock temp : list) {
		    	FooterUtil.setFooterMap("itemQty", temp.getItemQty(), footerMap);
		    	FooterUtil.setFooterMap("realQty", temp.getRealQty(), footerMap);
		    	FooterUtil.setFooterMap("outstockedQty", temp.getOutstockedQty(), footerMap);
		    }
		    // 返回合计列表
		    Map<String, Object> sumFootMap = new HashMap<String, Object>();
		    if (pageNo == 1) {
				if(pageSize >= total){
					sumFootMap.putAll(footerMap);
				}else{
					Map<String, Object> sumFoot = billOmOutstockManager.findSumQty(params,authorityParams);
					if(sumFoot.get("ITEM_QTY")!= null) {
						sumFootMap.put("itemQty", sumFoot.get("ITEM_QTY"));
					} else {
						 sumFootMap.put("itemQty", 0);
					}
					if(sumFoot.get("REAL_QTY")!= null) {
						sumFootMap.put("realQty", sumFoot.get("REAL_QTY"));
					} else {
						sumFootMap.put("realQty", 0);
					}
					if(sumFoot.get("OUTSTOCKED_QTY")!= null) {
						sumFootMap.put("outstockedQty", sumFoot.get("OUTSTOCKED_QTY"));
					} else {
						sumFootMap.put("outstockedQty", 0);
					}
					sumFootMap.put("isselectsum", true);
				}
				sumFootMap.put("outstockNo", "合计");
			    footerList.add(sumFootMap); 
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 移库回单
	 * @return
	 */
	@RequestMapping(value = "/toListPlanQuery")
	@InitpowerInterceptors
	public String toListPlanQuery() {
		return "billomoutstock/listPlanQuery";
	}

	@RequestMapping(value = "checkOutstock")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> checkOutstock(String locno, String outstockNo, String outstockName, String keyStr,HttpServletRequest req) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstockManager.checkOutstock(locno, outstockNo, outstockName, keyStr,user);
			obj.put("result", "success");
		} catch (ManagerException e) {
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	@RequestMapping(value = "/auditOutstock")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditOutstock(BillOmOutstock outstock, HttpServletRequest req) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			List<BillOmOutstock> oList = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
			}
			CurrentUser currentUser = new CurrentUser(req);
			billOmOutstockManager.auditOutstock(oList,currentUser.getLoginName(), currentUser.getUsername());
			obj.put("result", "success");
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
			return obj;
		}
	}
	
	
	@RequestMapping(value = "sendOrder")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> sendOrder(String locno, String assignName,String assignNameCh, String keyStr, HttpServletRequest req) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstockManager.sendOrder(locno, assignName, assignNameCh, keyStr,user);
			obj.put("result", "success");
		} catch (ManagerException e) {
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	@RequestMapping(value = "/all_list.json")
	@ResponseBody
	public  Map<String, Object> queryAllList(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, total,  total);
			List<BillOmOutstock> list = this.billOmOutstockManager.findByPage(page, sortColumn, sortOrder, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	/**
	 * 验证是否存在有实际数量的值
	 * @param req
	 * @param outstock
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectCheckDtlRealQtyEq")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectCheckDtlRealQtyEq(HttpServletRequest req,
			BillOmOutstock outstock) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		int total = 0;
		String outstockNo = "";
		try {
			List<BillOmOutstock> oList = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
			}
			
			for (BillOmOutstock billOmOutstock : oList) {
				total = this.billOmOutstockDtlManager.selectCheckDtlRealQtyEq(billOmOutstock);
				if(total > 0){
					outstockNo = billOmOutstock.getOutstockNo();
					break;
				}
			}
			obj.put("result", outstockNo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
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
	private List<BillOmOutstock> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<BillOmOutstock> entityClass = (Class<BillOmOutstock>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<BillOmOutstock> tl = new ArrayList<BillOmOutstock>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstock type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}
	
}